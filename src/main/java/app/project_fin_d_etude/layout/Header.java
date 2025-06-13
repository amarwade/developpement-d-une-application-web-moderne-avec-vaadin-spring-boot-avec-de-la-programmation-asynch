package app.project_fin_d_etude.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.model.Utilisateur;

public class Header extends HorizontalLayout {

    private static final String ROUTE_HOME = "/";
    private static final String ROUTE_ARTICLES = "/articles";
    private static final String ROUTE_ABOUT = "/about";
    private static final String ROUTE_CONTACT = "/contact";
    private static final String ROUTE_PROFILE = "/profile";
    private static final String ROUTE_LOGIN = "/login";

    private boolean isDarkMode = false;

    public Header() {
        setWidthFull();
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        addClassNames(
                LumoUtility.Background.CONTRAST_80,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BoxShadow.SMALL
        );
        getStyle().set("position", "relative");
        getStyle().set("top", "0");

        // Récupérer l'utilisateur connecté depuis la session
        Utilisateur currentUser = (Utilisateur) VaadinSession.getCurrent().getAttribute("user");

        // Logo ou lien vers le profil
        if (currentUser != null) {
            Anchor profileLink = new Anchor(ROUTE_PROFILE, currentUser.getNom());
            profileLink.addClassNames(
                    LumoUtility.FontSize.XLARGE,
                    LumoUtility.Margin.NONE,
                    LumoUtility.TextColor.PRIMARY,
                    LumoUtility.FontWeight.BOLD
            );
            profileLink.getStyle().set("text-decoration", "none");
            profileLink.getStyle().set("transition", "color 0.3s ease");
            profileLink.getStyle().set("cursor", "pointer");
            add(profileLink);
        } else {
            H3 logo = new H3("BIENVENUE!");

            logo.addClassNames(
                    LumoUtility.FontSize.XLARGE,
                    LumoUtility.Margin.NONE,
                    LumoUtility.FontWeight.BOLD
            );
            add(logo);
        }

        HorizontalLayout navLinks = new HorizontalLayout();
        navLinks.setSpacing(true);
        navLinks.addClassNames(LumoUtility.Gap.MEDIUM);

        // Création des liens de navigation
        createNavLink(navLinks, ROUTE_HOME, "Accueil");
        createNavLink(navLinks, ROUTE_ARTICLES, "Articles");
        createNavLink(navLinks, ROUTE_ABOUT, "A propos");
        createNavLink(navLinks, ROUTE_CONTACT, "Contact");

        // Conteneur pour les boutons
        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setSpacing(true);
        buttonsContainer.setAlignItems(Alignment.CENTER);

        // Bouton de thème
//        Button themeButton = new Button(new Icon(isDarkMode ? VaadinIcon.SUN_O : VaadinIcon.MOON_O));
//        themeButton.addClassNames(
//                LumoUtility.Background.CONTRAST_10,
//                LumoUtility.TextColor.PRIMARY,
//                LumoUtility.Padding.Horizontal.MEDIUM,
//                LumoUtility.Padding.Vertical.SMALL,
//                LumoUtility.BorderRadius.SMALL
//        );
//        themeButton.getStyle().set("cursor", "pointer");
//        themeButton.getElement().setAttribute("title", isDarkMode ? "Passer en mode clair" : "Passer en mode sombre");
//        themeButton.addClickListener(e -> {
//            isDarkMode = !isDarkMode;
//            themeButton.setIcon(new Icon(isDarkMode ? VaadinIcon.SUN_O : VaadinIcon.MOON_O));
//            themeButton.getElement().setAttribute("title", isDarkMode ? "Passer en mode clair" : "Passer en mode sombre");
//            getUI().ifPresent(ui -> {
//                if (isDarkMode) {
//                    ui.getElement().getThemeList().add("dark");
//                } else {
//                    ui.getElement().getThemeList().remove("dark");
//                }
//            });
//        });

        // Bouton de connexion ou déconnexion
        if (currentUser != null) {
            Button logoutButton = new Button("Déconnexion");
            logoutButton.addClassNames(
                    LumoUtility.Background.PRIMARY,
                    LumoUtility.TextColor.PRIMARY_CONTRAST,
                    LumoUtility.Padding.Horizontal.MEDIUM,
                    LumoUtility.Padding.Vertical.SMALL,
                    LumoUtility.BorderRadius.SMALL
            );
            logoutButton.getStyle().set("cursor", "pointer");
            logoutButton.getElement().setAttribute("title", "Se déconnecter de votre compte");
            logoutButton.addClickListener(e -> {
                VaadinSession.getCurrent().setAttribute("user", null);
                getUI().ifPresent(ui -> ui.navigate(ROUTE_HOME));
            });
//            buttonsContainer.add(themeButton, logoutButton);
            buttonsContainer.add(logoutButton);
        } else {
            Button loginButton = new Button("Connexion");
            loginButton.addClassNames(
                    LumoUtility.Background.PRIMARY,
                    LumoUtility.TextColor.PRIMARY_CONTRAST,
                    LumoUtility.Padding.Horizontal.MEDIUM,
                    LumoUtility.Padding.Vertical.SMALL,
                    LumoUtility.BorderRadius.SMALL
            );
            loginButton.getStyle().set("cursor", "pointer");
            loginButton.getElement().setAttribute("title", "Se connecter à votre compte");
            loginButton.addClickListener(e -> {
                getUI().ifPresent(ui -> ui.navigate(ROUTE_LOGIN));
            });
//            buttonsContainer.add(themeButton, loginButton);
            buttonsContainer.add(loginButton);

        }

        add(navLinks, buttonsContainer);
    }

    private void createNavLink(HorizontalLayout container, String route, String text) {
        Anchor link = new Anchor(route, text);
        link.addClassNames(
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.FontWeight.MEDIUM
        );
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("transition", "color 0.3s ease");
        link.getStyle().set("cursor", "pointer");
        container.add(link);
    }
}
