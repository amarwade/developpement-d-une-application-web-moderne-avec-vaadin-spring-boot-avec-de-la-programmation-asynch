package app.project_fin_d_etude.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.notification.Notification;

import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.SecurityUtils;
import app.project_fin_d_etude.utils.VaadinUtils;
import app.project_fin_d_etude.utils.ValidationUtils;
import app.project_fin_d_etude.repository.UtilisateurRepository;

@Route(value = "user/create-post", layout = MainLayout.class)
@PageTitle("Créer un post")
public class CreatePostView extends VerticalLayout implements PostPresenter.PostView {

    private final PostPresenter postPresenter;
    private final UtilisateurRepository utilisateurRepository;
    private TextField titleField;
    private TextArea contentArea;

    @Autowired
    public CreatePostView(PostPresenter postPresenter, UtilisateurRepository utilisateurRepository) {
        this.postPresenter = postPresenter;
        this.utilisateurRepository = utilisateurRepository;
        this.postPresenter.setView(this);
        configureLayout();
        add(createMainContent());
    }

    private void configureLayout() {
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setWidth("100%");
        mainContent.setAlignItems(Alignment.CENTER);

        mainContent.add(
                VaadinUtils.createSeparator("80%"),
                VaadinUtils.createPageTitle("CRÉER UN ARTICLE"),
                VaadinUtils.createSeparator("80%"),
                createUserInfoSection(),
                createPostForm()
        );
        return mainContent;
    }

    private Component createUserInfoSection() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        VerticalLayout section = new VerticalLayout();
        section.setWidth("50%");
        section.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.MEDIUM);
        section.setAlignItems(Alignment.CENTER);
        if (userEmail != null) {
            section.add(
                    new H3("👤 Auteur du post"),
                    new Paragraph("Vous allez créer cet article en tant que : " + userEmail)
            );
        } else {
            section.add(new Paragraph("Impossible de récupérer les informations de l'utilisateur connecté."));
        }
        return section;
    }

    private VerticalLayout createPostForm() {
        titleField = new TextField("Titre");
        titleField.setPlaceholder("Titre de l'article");
        titleField.setWidthFull();

        contentArea = new TextArea("Contenu");
        contentArea.setPlaceholder("Contenu de l'article");
        contentArea.setWidthFull();
        contentArea.setMinHeight("300px");

        Button publishButton = new Button("Publier", e -> publierArticle());
        publishButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout formLayout = new VerticalLayout(titleField, contentArea, publishButton);
        formLayout.setWidth("60%");
        return formLayout;
    }

    private void publierArticle() {
        if (!validerFormulaire()) {
            return;
        }

        Post post = new Post();
        post.setTitre(titleField.getValue().trim());
        post.setContenu(contentArea.getValue().trim());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            Notification.show("Impossible de récupérer les informations de l'utilisateur connecté.");
            return;
        }

        String email = oidcUser.getEmail();
        if (email == null) {
            Notification.show("L'email de l'utilisateur connecté est introuvable.");
            return;
        }

        // Find or create user
        Utilisateur auteur = utilisateurRepository.findByEmail(email)
                .orElseGet(() -> {
                    Utilisateur newUser = new Utilisateur();
                    newUser.setEmail(email);

                    String nom = oidcUser.getGivenName();
                    String prenom = oidcUser.getFamilyName();
                    String displayName = ((nom != null ? nom : "") + " " + (prenom != null ? prenom : "")).trim();
                    newUser.setNom(displayName.isBlank() ? oidcUser.getPreferredUsername() : displayName);

                    newUser.setActif(true);
                    newUser.setRole(Utilisateur.Role.UTILISATEUR);
                    newUser.setDateCreation(java.time.LocalDateTime.now());
                    return utilisateurRepository.save(newUser);
                });

        post.setAuteur(auteur);

        VaadinUtils.showLoading(this);
        postPresenter.publierPost(post);
    }

    private boolean validerFormulaire() {
        ValidationUtils.ValidationResult titleResult = ValidationUtils.validateTitle(titleField);
        if (!titleResult.isValid()) {
            VaadinUtils.showErrorNotification(titleResult.getErrorMessage());
            return false;
        }

        ValidationUtils.ValidationResult contentResult = ValidationUtils.validateContent(contentArea);
        if (!contentResult.isValid()) {
            VaadinUtils.showErrorNotification(contentResult.getErrorMessage());
            return false;
        }

        return true;
    }

    @Override
    public void afficherMessage(String message) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.showSuccessNotification(message);
            viderFormulaire();
        }));
    }

    @Override
    public void afficherErreur(String erreur) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.showErrorNotification(erreur);
        }));
    }

    @Override
    public void viderFormulaire() {
        getUI().ifPresent(ui -> ui.access(() -> {
            titleField.clear();
            contentArea.clear();
        }));
    }

    @Override
    public void redirigerVersDetail(Long postId) {
        getUI().ifPresent(ui -> ui.access(() -> {
            ui.navigate("user/article/" + postId);
        }));
    }

    // Méthodes non utilisées
    @Override
    public void afficherPost(Post post) {
    }

    @Override
    public void afficherPosts(List<Post> posts) {
    }

    @Override
    public void mettreAJourPagination(int totalItems) {
    }
}
