package app.project_fin_d_etude.views.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.AdminLayout;
import app.project_fin_d_etude.model.Profile;
import app.project_fin_d_etude.presenter.AdminPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

/**
 * Vue d'administration pour gérer les profils utilisateurs. Accessible via la
 * route "/admin/profiles".
 */
@Route(value = "admin/profiles", layout = AdminLayout.class)
@PageTitle("Gestion des profils - Administration")
public class AdminUsersView extends VerticalLayout implements AdminPresenter.AdminView {

    private final AdminPresenter adminPresenter;
    private VerticalLayout profilesContainer;

    @Autowired
    public AdminUsersView(AdminPresenter adminPresenter) {
        this.adminPresenter = adminPresenter;
        this.adminPresenter.setView(this);

        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);

        add(createMainContent());
        loadProfiles();
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
        mainContent.add(createProfilesSection());

        return mainContent;
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("GESTION DES PROFILS UTILISATEURS");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private VerticalLayout createProfilesSection() {
        VerticalLayout profilesSection = new VerticalLayout();
        profilesSection.setWidth("90%");
        profilesSection.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM
        );

        H2 profilesTitle = new H2("Liste des profils");
        profilesTitle.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        profilesContainer = new VerticalLayout();
        profilesContainer.setSpacing(true);
        profilesContainer.setWidthFull();

        profilesSection.add(profilesTitle, profilesContainer);
        return profilesSection;
    }

    private void loadProfiles() {
        adminPresenter.chargerProfiles();
    }

    @Override
    public void afficherProfiles(List<Profile> profiles) {
        profilesContainer.removeAll();

        if (profiles.isEmpty()) {
            Paragraph noProfiles = new Paragraph("Aucun profil trouvé.");
            noProfiles.addClassNames(
                    LumoUtility.TextColor.SECONDARY,
                    LumoUtility.Margin.Top.MEDIUM
            );
            profilesContainer.add(noProfiles);
        } else {
            for (Profile profile : profiles) {
                profilesContainer.add(createProfileCard(profile));
            }
        }
    }

    private VerticalLayout createProfileCard(Profile profile) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST,
                LumoUtility.Margin.Bottom.SMALL
        );

        String userName = profile.getUtilisateur() != null
                ? profile.getUtilisateur().getNom()
                : "Utilisateur inconnu";

        Paragraph nameParagraph = new Paragraph("Nom: " + userName);
        nameParagraph.addClassNames(LumoUtility.FontWeight.BOLD);

        Paragraph bioParagraph = new Paragraph("Bio: " + (profile.getBio() != null ? profile.getBio() : "Non renseignée"));

        card.add(nameParagraph, bioParagraph);
        return card;
    }

    @Override
    public void afficherMessage(String message) {
        VaadinUtils.showSuccessNotification(message);
    }

    @Override
    public void afficherErreur(String erreur) {
        VaadinUtils.showErrorNotification(erreur);
    }
}
