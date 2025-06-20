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
import java.util.function.Consumer;

public class BlogPostCard extends Div {

    public BlogPostCard(Post post, Consumer<Post> onDetailClick) {
        addClassNames(
                LumoUtility.Background.CONTRAST_80,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.MEDIUM
        );
        getStyle().set("border", "1px solid var(--lumo-contrast-10%)");

        H3 cardTitle = new H3(post.getTitre());
        cardTitle.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        // Auteur et date
        String auteur = post.getAuteur() != null ? post.getAuteur().getNom() : "Auteur inconnu";
        String date = post.getDatePublication() != null ? post.getDatePublication().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Date inconnue";
        Paragraph cardMeta = new Paragraph("Par " + auteur + " • " + date);
        cardMeta.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Margin.Top.SMALL,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.TextColor.SECONDARY
        );

        // Extrait du contenu (200 caractères max)
        String contenu = post.getContenu();
        String extrait = contenu.length() > 200 ? contenu.substring(0, 200) + "..." : contenu;
        Paragraph cardDescription = new Paragraph(extrait);
        cardDescription.addClassNames(LumoUtility.FontSize.MEDIUM);

        Button detailButton = new Button("Voir le détail", e -> onDetailClick.accept(post));
        detailButton.addClassNames(
                LumoUtility.Margin.Top.MEDIUM,
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.Padding.Vertical.XSMALL,
                LumoUtility.BorderRadius.SMALL
        );

        VerticalLayout cardContent = new VerticalLayout(cardTitle, cardMeta, cardDescription, detailButton);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        cardContent.setAlignItems(FlexComponent.Alignment.START);

        add(cardContent);
    }
}
