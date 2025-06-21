package app.project_fin_d_etude.views;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.CommentairePresenter;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;
import app.project_fin_d_etude.utils.ValidationUtils;

/**
 * Vue de détail d'un article : affiche le contenu de l'article et ses
 * commentaires. L'affichage est automatique dès le chargement de la page.
 */
@Route(value = "user/article", layout = MainLayout.class)
@PageTitle("Détail de l'article")
public class PostDetailView extends VerticalLayout implements HasUrlParameter<Long>, PostPresenter.PostView, CommentairePresenter.CommentaireView {

    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final String NO_COMMENTS_MESSAGE = "Aucun commentaire pour le moment. Soyez le premier à commenter !";
    private static final String COMMENT_PLACEHOLDER = "Écrivez votre commentaire ici...";
    private static final String DEFAULT_AUTHOR_NAME = "Anonyme";

    private final PostPresenter postPresenter;
    private final CommentairePresenter commentairePresenter;
    private final DateTimeFormatter dateFormatter;
    private VerticalLayout commentsSection;
    private TextArea commentTextArea;
    private Button submitButton;
    private Post currentPost;

    @Autowired
    public PostDetailView(PostPresenter postPresenter, CommentairePresenter commentairePresenter) {
        this.postPresenter = postPresenter;
        this.commentairePresenter = commentairePresenter;
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.postPresenter.setView(this);
        this.commentairePresenter.setView(this);
    }

    /**
     * Récupère l'identifiant de l'article depuis l'URL et déclenche le
     * chargement.
     */
    @Override
    public void setParameter(BeforeEvent event, Long postId) {
        if (postId == null) {
            getUI().ifPresent(ui -> ui.navigate("articles"));
            VaadinUtils.showErrorNotification("ID d'article invalide");
            return;
        }
        removeAll(); // Nettoyer la vue avant de charger
        VaadinUtils.showLoading(this);
        postPresenter.chargerPost(postId);
    }

    /**
     * Affiche le contenu de l'article et déclenche le chargement des
     * commentaires.
     */
    @Override
    public void afficherPost(Post post) {
        getUI().ifPresent(ui -> ui.access(() -> {
            if (post == null) {
                showErrorAndRedirect("Article introuvable ou supprimé");
                return;
            }
            this.currentPost = post;
            renderPostContent(post);
            // Le chargement des commentaires est déclenché ici
            commentairePresenter.chargerCommentaires(post);
        }));
    }

    /**
     * Construit et affiche le contenu principal de l'article.
     */
    private void renderPostContent(Post post) {
        removeAll();

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setWidth("70%");
        mainContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainContent.addClassNames(LumoUtility.Padding.Vertical.LARGE);

        mainContent.add(
                createPostHeader(post),
                createPostBody(post.getContenu()),
                createCommentsContainer()
        );

        add(mainContent);
    }

    /**
     * Crée l'en-tête de l'article (titre, métadonnées).
     */
    private VerticalLayout createPostHeader(Post post) {
        VerticalLayout header = new VerticalLayout();
        header.add(
                createPostTitle(post.getTitre()),
                createPostMetadata(post)
        );
        return header;
    }

    /**
     * Crée le titre de l'article.
     */
    private H1 createPostTitle(String title) {
        H1 titleComponent = new H1(title);
        titleComponent.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return titleComponent;
    }

    /**
     * Crée les métadonnées de l'article (auteur, date).
     */
    private HorizontalLayout createPostMetadata(Post post) {
        String authorName = Optional.ofNullable(post.getAuteur())
                .map(a -> a.getNom())
                .orElse(DEFAULT_AUTHOR_NAME);

        HorizontalLayout metadata = new HorizontalLayout(
                new Paragraph("Par " + authorName),
                new Paragraph(post.getDatePublication().format(dateFormatter))
        );
        metadata.setSpacing(true);
        return metadata;
    }

    /**
     * Crée le corps de l'article.
     */
    private Div createPostBody(String content) {
        Div contentDiv = new Div(new Paragraph(content));
        contentDiv.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Margin.Bottom.LARGE
        );
        contentDiv.setWidthFull();
        return contentDiv;
    }

    /**
     * Crée la section des commentaires (formulaire + liste).
     */
    private VerticalLayout createCommentsContainer() {
        VerticalLayout container = new VerticalLayout(
                new H2("Commentaires"),
                createCommentInputForm(),
                commentsSection = new VerticalLayout()
        );
        container.setWidthFull();
        return container;
    }

    /**
     * Crée le formulaire de saisie de commentaire.
     */
    private VerticalLayout createCommentInputForm() {
        commentTextArea = new TextArea();
        commentTextArea.setPlaceholder(COMMENT_PLACEHOLDER);
        commentTextArea.setWidthFull();

        submitButton = new Button("Publier", e -> handleCommentSubmission());

        VerticalLayout formLayout = new VerticalLayout(commentTextArea, submitButton);
        formLayout.setWidthFull();
        return formLayout;
    }

    /**
     * Gère la soumission d'un commentaire (validation, désactivation bouton,
     * feedback).
     */
    private void handleCommentSubmission() {
        submitButton.setEnabled(false);
        ValidationUtils.ValidationResult validation = ValidationUtils.validateContent(commentTextArea);
        if (!validation.isValid()) {
            VaadinUtils.showErrorNotification(validation.getErrorMessage());
            submitButton.setEnabled(true);
            return;
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(commentTextArea.getValue().trim());
        commentaire.setPost(currentPost);
        commentairePresenter.ajouter(commentaire);
    }

    /**
     * Affiche la liste des commentaires ou un message s'il n'y en a pas.
     */
    @Override
    public void afficherCommentaires(List<Commentaire> commentaires) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.hideLoading(this);
            commentsSection.removeAll();

            if (commentaires.isEmpty()) {
                commentsSection.add(new Paragraph(NO_COMMENTS_MESSAGE));
            } else {
                commentaires.forEach(comment
                        -> commentsSection.add(createCommentCard(comment))
                );
            }
        }));
    }

    /**
     * Crée une carte de commentaire.
     */
    private Div createCommentCard(Commentaire commentaire) {
        String authorName = Optional.ofNullable(commentaire.getAuteur())
                .map(a -> a.getNom())
                .orElse(DEFAULT_AUTHOR_NAME);

        Div card = new Div(
                new Paragraph(authorName + " - " + commentaire.getDateCreation().format(dateFormatter)),
                new Paragraph(commentaire.getContenu())
        );
        card.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Margin.Bottom.SMALL
        );
        return card;
    }

    /**
     * Affiche un message de succès et vide le champ commentaire.
     */
    @Override
    public void afficherMessage(String message) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.showSuccessNotification(message);
            commentTextArea.clear();
            submitButton.setEnabled(true);
        }));
    }

    /**
     * Affiche un message d'erreur.
     */
    @Override
    public void afficherErreur(String erreur) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.showErrorNotification(erreur);
            submitButton.setEnabled(true);
        }));
    }

    /**
     * Rafraîchit la liste des commentaires après ajout.
     */
    @Override
    public void rafraichirListe() {
        getUI().ifPresent(ui -> ui.access(() -> {
            if (currentPost != null) {
                commentairePresenter.chargerCommentaires(currentPost);
            }
        }));
    }

    /**
     * Affiche une erreur et redirige vers la liste des articles.
     */
    private void showErrorAndRedirect(String errorMessage) {
        VaadinUtils.showErrorNotification(errorMessage);
        getUI().ifPresent(ui -> ui.navigate("articles"));
    }

    // Méthodes non utilisées de PostView
    @Override
    public void afficherPosts(List<Post> posts) {
    }

    @Override
    public void viderFormulaire() {
    }

    @Override
    public void redirigerVersDetail(Long postId) {
    }

    @Override
    public void mettreAJourPagination(int totalItems) {
    }
}
