package app.project_fin_d_etude.components;

import app.project_fin_d_etude.model.Post;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;

public class BlogPostCard extends Div {

    // Longueur maximale de l'extrait du contenu affiché
    private static final int MAX_EXCERPT_LENGTH = 200;
    // Format d'affichage de la date de publication
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Construit une carte d'aperçu d'un article de blog.
     *
     * @param post L'article à afficher
     * @param onDetailClick Action à exécuter lors du clic sur le bouton "Voir
     * le détail"
     */
    public BlogPostCard(Post post, Consumer<Post> onDetailClick) {
        // Style général de la carte
        addClassNames(
                LumoUtility.Background.CONTRAST_80,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.MEDIUM
        );
        getStyle().set("border", "1px solid var(--lumo-contrast-10%)");

        // Création des sous-composants de la carte
        H3 cardTitle = createTitle(post);
        Paragraph cardMeta = createMeta(post);
        Paragraph cardDescription = createDescription(post);
        Button detailButton = createDetailButton(post, onDetailClick);

        // Organisation verticale des éléments
        VerticalLayout cardContent = new VerticalLayout(cardTitle, cardMeta, cardDescription, detailButton);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        cardContent.setAlignItems(FlexComponent.Alignment.START);

        add(cardContent);
    }

    /**
     * Crée le titre de la carte à partir du titre du post.
     */
    private H3 createTitle(Post post) {
        H3 title = new H3(Optional.ofNullable(post.getTitre()).orElse("Titre inconnu"));
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        return title;
    }

    /**
     * Crée la ligne d'information auteur/date.
     */
    private Paragraph createMeta(Post post) {
        String auteur = Optional.ofNullable(post.getAuteur())
                .map(a -> Optional.ofNullable(a.getNom()).orElse("Auteur inconnu"))
                .orElse("Auteur inconnu");
        String date = Optional.ofNullable(post.getDatePublication())
                .map(d -> d.format(DATE_FORMATTER))
                .orElse("Date inconnue");
        Paragraph meta = new Paragraph("Par " + auteur + " • " + date);
        meta.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Margin.Top.SMALL,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.TextColor.SECONDARY
        );
        return meta;
    }

    /**
     * Crée l'extrait du contenu de l'article (tronqué si nécessaire).
     */
    private Paragraph createDescription(Post post) {
        String contenu = Optional.ofNullable(post.getContenu()).orElse("");
        String extrait = contenu.length() > MAX_EXCERPT_LENGTH ? contenu.substring(0, MAX_EXCERPT_LENGTH) + "..." : contenu;
        Paragraph description = new Paragraph(extrait);
        description.addClassNames(LumoUtility.FontSize.MEDIUM);
        return description;
    }

    /**
     * Crée le bouton permettant d'accéder au détail de l'article.
     */
    private Button createDetailButton(Post post, Consumer<Post> onDetailClick) {
        Button button = new Button("Voir le détail", e -> onDetailClick.accept(post));
        button.addClassNames(
                LumoUtility.Margin.Top.MEDIUM,
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.Padding.Vertical.XSMALL,
                LumoUtility.BorderRadius.SMALL
        );
        return button;
    }
}
