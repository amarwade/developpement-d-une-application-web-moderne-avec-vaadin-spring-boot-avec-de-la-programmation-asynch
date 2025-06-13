package app.project_fin_d_etude.view;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
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

/**
 * Vue détaillée d'un article avec ses commentaires. Cette vue est accessible
 * via la route "/user/article/:id".
 */
@Route(value = "user/article/:id", layout = MainLayout.class)
@PageTitle("Détail de l'article")
public class PostDetailView extends VerticalLayout implements HasUrlParameter<Long>, CommentairePresenter.CommentaireView {

    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final int NOTIFICATION_DURATION = 3000;
    private static final String NO_COMMENTS_MESSAGE = "Aucun commentaire pour le moment. Soyez le premier à commenter !";
    private static final String COMMENT_PLACEHOLDER = "Écrivez votre commentaire ici...";

    private final PostPresenter postPresenter;
    private final CommentairePresenter commentairePresenter;
    private final DateTimeFormatter dateFormatter;
    private Post currentPost;
    private VerticalLayout commentsSection;
    private TextArea commentTextArea;

    @Autowired
    public PostDetailView(PostPresenter postPresenter, CommentairePresenter commentairePresenter) {
        this.postPresenter = postPresenter;
        this.commentairePresenter = commentairePresenter;
        this.commentairePresenter.setView(this);
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        configureLayout();
    }

    private void configureLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
    }

    @Override
    public void setParameter(BeforeEvent event, Long postId) {
        postPresenter.chargerPost(postId);
    }

    public void afficherPost(Post post) {
        this.currentPost = post;
        removeAll();
        add(createMainContent(post));
        commentairePresenter.chargerCommentaires(post);
    }
    //
    private VerticalLayout createMainContent(Post post) {
        VerticalLayout mainContent = createSection("80%", FlexComponent.Alignment.CENTER);
        mainContent.addClassNames(LumoUtility.Padding.Vertical.LARGE);

        mainContent.add(
                createPostTitle(post.getTitre()),
                createPostMetadata(post),
                createPostContent(post.getContenu()),
                createCommentsSection()
        );

        return mainContent;
    }

    private VerticalLayout createSection(String width, FlexComponent.Alignment alignment) {
        VerticalLayout section = new VerticalLayout();
        section.setWidth(width);
        section.setPadding(true);
        section.setAlignItems(alignment);
        section.addClassNames(
                LumoUtility.Margin.AUTO,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        return section;
    }

    private H1 createPostTitle(String title) {
        H1 pageTitle = new H1(title);
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private HorizontalLayout createPostMetadata(Post post) {
        HorizontalLayout metadata = new HorizontalLayout();
        metadata.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);
        metadata.setSpacing(true);

        metadata.add(
                new Paragraph("Par " + post.getAuteur().getNom()),
                new Paragraph(post.getDatePublication().format(dateFormatter)),
                new Paragraph(post.getCategorie().name())
        );

        return metadata;
    }

    private Div createPostContent(String content) {
        Div articleContent = new Div();
        articleContent.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        articleContent.setWidthFull();

        Paragraph contentParagraph = new Paragraph(content);
        contentParagraph.addClassNames(LumoUtility.FontSize.MEDIUM);
        articleContent.add(contentParagraph);

        return articleContent;
    }

    private VerticalLayout createCommentsSection() {
        commentsSection = new VerticalLayout();
        commentsSection.setWidthFull();
        commentsSection.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.LARGE);
        commentsSection.add(createCommentsTitle(), createCommentForm());
        return commentsSection;
    }

    private H2 createCommentsTitle() {
        H2 commentsTitle = new H2("Commentaires");
        commentsTitle.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.PRIMARY_50,
                LumoUtility.Padding.Bottom.SMALL
        );
        return commentsTitle;
    }

    private VerticalLayout createCommentForm() {
        VerticalLayout commentInputLayout = new VerticalLayout();
        commentInputLayout.setWidthFull();
        commentInputLayout.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        commentTextArea = createCommentTextArea();
        Button sendButton = createSendButton();

        commentInputLayout.add(commentTextArea, sendButton);
        return commentInputLayout;
    }

    private TextArea createCommentTextArea() {
        TextArea textArea = new TextArea("Votre commentaire");
        textArea.setWidthFull();
        textArea.setHeight("100px");
        textArea.setPlaceholder(COMMENT_PLACEHOLDER);
        textArea.addClassNames(LumoUtility.Background.BASE, LumoUtility.TextColor.PRIMARY);
        return textArea;
    }

    private Button createSendButton() {
        Button button = new Button("Publier");
        button.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.BorderRadius.SMALL,
                LumoUtility.Margin.Top.SMALL
        );
        button.addClickListener(e -> publierCommentaire());
        return button;
    }

    private void publierCommentaire() {
        String commentText = commentTextArea.getValue();
        if (commentText != null && !commentText.trim().isEmpty()) {
            Commentaire commentaire = new Commentaire();
            commentaire.setContenu(commentText);
            commentaire.setPost(currentPost);
            // TODO: Définir l'auteur du commentaire (utilisateur connecté)
            commentairePresenter.ajouter(commentaire);
            commentTextArea.clear();
        }
    }

    @Override
    public void afficherCommentaires(List<Commentaire> commentaires) {
        commentsSection.removeAll();
        commentsSection.add(createCommentsTitle());

        if (commentaires.isEmpty()) {
            Paragraph noComments = new Paragraph(NO_COMMENTS_MESSAGE);
            noComments.addClassNames(LumoUtility.TextColor.PRIMARY);
            commentsSection.add(noComments);
        } else {
            commentaires.forEach(commentaire
                    -> commentsSection.add(createCommentCard(
                            commentaire.getAuteur().getNom(),
                            commentaire.getContenu(),
                            commentaire.getDateCreation().format(dateFormatter)
                    ))
            );
        }

        commentsSection.add(createCommentForm());
    }

    private Div createCommentCard(String author, String commentText, String date) {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Margin.Bottom.SMALL,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        Paragraph authorParagraph = new Paragraph(author);
        authorParagraph.addClassNames(LumoUtility.FontWeight.BOLD);

        Paragraph dateParagraph = new Paragraph(date);
        dateParagraph.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        Paragraph contentParagraph = new Paragraph(commentText);
        contentParagraph.addClassNames(LumoUtility.Margin.Top.SMALL);

        card.add(authorParagraph, dateParagraph, contentParagraph);
        return card;
    }

    @Override
    public void afficherMessage(String message) {
        Notification notification = Notification.show(message, NOTIFICATION_DURATION, Notification.Position.TOP_CENTER);
        notification.addThemeName("success");
    }

    @Override
    public void afficherErreur(String erreur) {
        Notification notification = Notification.show(erreur, NOTIFICATION_DURATION, Notification.Position.TOP_CENTER);
        notification.addThemeName("error");
    }

    @Override
    public void rafraichirListe() {
        if (currentPost != null) {
            commentairePresenter.chargerCommentaires(currentPost);
        }
    }
}
