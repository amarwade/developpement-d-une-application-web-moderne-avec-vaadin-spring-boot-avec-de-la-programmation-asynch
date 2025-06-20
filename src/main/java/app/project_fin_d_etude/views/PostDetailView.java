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
    private Post currentPost;

    @Autowired
    public PostDetailView(PostPresenter postPresenter, CommentairePresenter commentairePresenter) {
        this.postPresenter = postPresenter;
        this.commentairePresenter = commentairePresenter;
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.postPresenter.setView(this);
        this.commentairePresenter.setView(this);

    }

    @Override
    public void setParameter(BeforeEvent event, Long postId) {
        if (postId == null) {
            showErrorAndRedirect("ID d'article invalide");
            return;
        }
        postPresenter.chargerPost(postId);
    }

    @Override
    public void afficherPost(Post post) {
        getUI().ifPresent(ui -> ui.access(() -> {
            if (post == null) {
                showErrorAndRedirect("Article introuvable ou supprimé");
                return;
            }
            this.currentPost = post;
            renderPostContent(post);
        }));
    }

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
        commentairePresenter.chargerCommentaires(post);
    }

    private VerticalLayout createPostHeader(Post post) {
        VerticalLayout header = new VerticalLayout();
        header.add(
                createPostTitle(post.getTitre()),
                createPostMetadata(post)
        );
        return header;
    }

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

    private VerticalLayout createCommentsContainer() {
        VerticalLayout container = new VerticalLayout(
                new H2("Commentaires"),
                createCommentInputForm(),
                commentsSection = new VerticalLayout()
        );
        container.setWidthFull();
        return container;
    }

    private VerticalLayout createCommentInputForm() {
        commentTextArea = new TextArea();
        commentTextArea.setPlaceholder(COMMENT_PLACEHOLDER);
        commentTextArea.setWidthFull();

        Button submitButton = new Button("Publier", e -> handleCommentSubmission());

        VerticalLayout formLayout = new VerticalLayout(commentTextArea, submitButton);
        formLayout.setWidthFull();
        return formLayout;
    }

    private void handleCommentSubmission() {
        ValidationUtils.ValidationResult validation = ValidationUtils.validateContent(commentTextArea);
        if (!validation.isValid()) {
            VaadinUtils.showErrorNotification(validation.getErrorMessage());
            return;
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(commentTextArea.getValue().trim());
        commentaire.setPost(currentPost);
        commentairePresenter.ajouter(commentaire);
    }

    @Override
    public void afficherCommentaires(List<Commentaire> commentaires) {
        getUI().ifPresent(ui -> ui.access(() -> {
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

    @Override
    public void afficherMessage(String message) {
        VaadinUtils.showSuccessNotification(message);
        commentTextArea.clear();
    }

    @Override
    public void afficherErreur(String erreur) {
        VaadinUtils.showErrorNotification(erreur);
    }

    @Override
    public void rafraichirListe() {
        if (currentPost != null) {
            commentairePresenter.chargerCommentaires(currentPost);
        }
    }

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
