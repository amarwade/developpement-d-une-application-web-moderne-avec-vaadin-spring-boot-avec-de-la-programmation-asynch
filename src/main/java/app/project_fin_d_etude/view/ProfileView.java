package app.project_fin_d_etude.view;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Profile;
import app.project_fin_d_etude.model.Utilisateur;
import app.project_fin_d_etude.service.ProfileService;
import app.project_fin_d_etude.service.UtilisateurService;

/**
 * Vue du profil utilisateur. Affiche les informations de l'utilisateur et un
 * bouton pour créer un article.
 */
@Route(value = "user/profile", layout = MainLayout.class)
@PageTitle("Mon Profil")
public class ProfileView extends VerticalLayout {

    private final UtilisateurService utilisateurService;
    private final ProfileService profileService;
    private Paragraph usernameParagraph;
    private Paragraph emailParagraph;
    private Paragraph roleParagraph;
    private Paragraph bioParagraph;
    private Paragraph descriptionParagraph;

    @Autowired
    public ProfileView(UtilisateurService utilisateurService, ProfileService profileService) {
        this.utilisateurService = utilisateurService;
        this.profileService = profileService;

        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);

        add(new H1("Mon Profil"));

        usernameParagraph = new Paragraph();
        emailParagraph = new Paragraph();
        roleParagraph = new Paragraph();
        bioParagraph = new Paragraph();
        descriptionParagraph = new Paragraph();

        add(usernameParagraph, emailParagraph, roleParagraph, new H3("Biographie"), bioParagraph, new H3("Description"), descriptionParagraph);

        Button createPostButton = new Button("Créer un post");
        createPostButton.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.Margin.Top.MEDIUM
        );
        createPostButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("user/create-post")));
        add(createPostButton);

        loadUserProfile();
    }

    private void loadUserProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<Utilisateur> utilisateurOptional = utilisateurService.findByEmail(username);

            if (utilisateurOptional.isPresent()) {
                Utilisateur user = utilisateurOptional.get();
                usernameParagraph.setText("Nom: " + user.getNom());
                emailParagraph.setText("Email: " + user.getEmail());
                roleParagraph.setText("Rôle: " + user.getRole().name());

                Optional<Profile> profileOptional = profileService.getProfileByUtilisateur(user);
                if (profileOptional.isPresent()) {
                    Profile profile = profileOptional.get();
                    bioParagraph.setText("Bio: " + (profile.getBio() != null ? profile.getBio() : "Non renseignée"));
                    descriptionParagraph.setText("Description: " + (profile.getDescription() != null ? profile.getDescription() : "Non renseignée"));
                } else {
                    bioParagraph.setText("Bio: Non disponible");
                    descriptionParagraph.setText("Description: Non disponible");
                }
            } else {
                usernameParagraph.setText("Utilisateur non trouvé.");
                emailParagraph.setText("");
                roleParagraph.setText("");
                bioParagraph.setText("");
                descriptionParagraph.setText("");
            }
        } else {
            usernameParagraph.setText("Veuillez vous connecter pour voir votre profil.");
            emailParagraph.setText("");
            roleParagraph.setText("");
            bioParagraph.setText("");
            descriptionParagraph.setText("");
        }
    }
}
