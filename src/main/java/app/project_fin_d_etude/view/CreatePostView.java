package app.project_fin_d_etude.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.CategoriePost;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.PostPresenter;

/**
 * Vue permettant la création d'un nouvel article. Cette vue est accessible via
 * la route "/user/create-post".
 */
@Route(value = "user/create-post", layout = MainLayout.class)
@PageTitle("Créer un post")
public class CreatePostView extends VerticalLayout implements PostPresenter.PostView {

    private static final int NOTIFICATION_DURATION = 3000;
    private static final String TITLE_ERROR = "Veuillez entrer un titre";
    private static final String CATEGORY_ERROR = "Veuillez sélectionner une catégorie";
    private static final String CONTENT_ERROR = "Veuillez entrer un contenu";

    private final PostPresenter postPresenter;
    private TextField titleField;
    private ComboBox<CategoriePost> categoryComboBox;
    private TextArea contentArea;

    @Autowired
    public CreatePostView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        this.postPresenter.setView(this);
        configureLayout();
        add(createMainContent());
        postPresenter.chargerCategories();
    }

    private void configureLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setWidth("100%");
        mainContent.setPadding(true);
        mainContent.setAlignItems(FlexComponent.Alignment.CENTER);
        mainContent.addClassNames(
                LumoUtility.Margin.AUTO,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL
        );

        mainContent.add(createPageTitle());
        mainContent.add(createPostForm());
        return mainContent;
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("CRÉER UN ARTICLE");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private VerticalLayout createPostForm() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth("50%");
        formLayout.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        formLayout.setSpacing(true);

        initializeFormFields();
        formLayout.add(titleField, categoryComboBox, contentArea);
        formLayout.add(createPublishButton());

        return formLayout;
    }

    private void initializeFormFields() {
        titleField = createTextField("Titre", "Titre de l'article");
        categoryComboBox = createCategoryComboBox();
        contentArea = createContentArea();
    }

    private TextField createTextField(String label, String placeholder) {
        TextField field = new TextField(label);
        field.setPlaceholder(placeholder);
        field.setWidthFull();
        field.setRequired(true);
        field.setRequiredIndicatorVisible(true);
        field.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.TextColor.PRIMARY
        );
        return field;
    }

    private ComboBox<CategoriePost> createCategoryComboBox() {
        ComboBox<CategoriePost> comboBox = new ComboBox<>("Catégorie");
        comboBox.setPlaceholder("Sélectionnez une catégorie");
        comboBox.setWidthFull();
        comboBox.setRequired(true);
        comboBox.setRequiredIndicatorVisible(true);
        comboBox.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.TextColor.PRIMARY
        );
        return comboBox;
    }

    private TextArea createContentArea() {
        TextArea area = new TextArea("Contenu");
        area.setPlaceholder("Contenu de l'article");
        area.setWidthFull();
        area.setHeight("300px");
        area.setRequired(true);
        area.setRequiredIndicatorVisible(true);
        area.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.TextColor.PRIMARY
        );
        return area;
    }

    private Button createPublishButton() {
        Button button = new Button("Valider");
        button.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.AUTO
        );
        button.addClickListener(e -> publierArticle());
        return button;
    }

    private void publierArticle() {
        if (!validerFormulaire()) {
            return;
        }

        Post post = new Post();
        post.setTitre(titleField.getValue());
        post.setContenu(contentArea.getValue());
        post.setCategorie(categoryComboBox.getValue());

        postPresenter.publier(post);
    }

    private boolean validerFormulaire() {
        if (!validerChamp(titleField, TITLE_ERROR)) {
            return false;
        }

        if (!validerCategorie()) {
            return false;
        }

        if (!validerChamp(contentArea, CONTENT_ERROR)) {
            return false;
        }

        return true;
    }

    private boolean validerChamp(TextField field, String messageErreur) {
        if (field.getValue() == null || field.getValue().trim().isEmpty()) {
            afficherNotification(messageErreur, true);
            return false;
        }
        return true;
    }

    private boolean validerChamp(TextArea area, String messageErreur) {
        if (area.getValue() == null || area.getValue().trim().isEmpty()) {
            afficherNotification(messageErreur, true);
            return false;
        }
        return true;
    }

    private boolean validerCategorie() {
        if (categoryComboBox.getValue() == null) {
            afficherNotification(CATEGORY_ERROR, true);
            return false;
        }
        return true;
    }

    private void afficherNotification(String message, boolean isError) {
        Notification notification = Notification.show(message, NOTIFICATION_DURATION, Notification.Position.TOP_CENTER);
        if (isError) {
            notification.addThemeName("error");
        }
    }

    @Override
    public void afficherCategories(List<CategoriePost> categories) {
        categoryComboBox.setItems(categories);
        categoryComboBox.setItemLabelGenerator(CategoriePost::name);
    }

    @Override
    public void afficherPosts(List<Post> posts) {
        // Non utilisé dans cette vue
    }

    @Override
    public void afficherMessage(String message) {
        afficherNotification(message, false);
    }

    @Override
    public void afficherErreur(String erreur) {
        afficherNotification(erreur, true);
    }

    @Override
    public void viderFormulaire() {
        titleField.clear();
        categoryComboBox.clear();
        contentArea.clear();
    }

    @Override
    public void redirigerVersDetail(Long postId) {
        getUI().ifPresent(ui -> ui.navigate("article/" + postId));
    }
}
