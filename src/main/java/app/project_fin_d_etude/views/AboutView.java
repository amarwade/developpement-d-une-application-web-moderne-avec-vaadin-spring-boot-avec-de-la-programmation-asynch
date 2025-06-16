package app.project_fin_d_etude.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.MainLayout;

/**
 * Vue "À propos" présentant les informations sur le site et le développeur.
 * Cette vue est accessible via la route "/about".
 */
@Route(value = "about", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Padding.LARGE);

        // Conteneur principal
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth("80%");
        mainContainer.setMaxWidth("1200px");
        mainContainer.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Padding.LARGE
        );

        //ajouter une ligne de séparation
        HorizontalLayout separator = new HorizontalLayout();
        separator.setWidth("100%");
        separator.setHeight("2px");
        separator.getStyle().set("background-color", "lightgray");
        separator.addClassNames("separator");
        mainContainer.add(separator);
        // Titre principal
        H1 title = new H1("À PROPOS DU SITE");
        title.setMaxWidth("1159px");
        title.addClassNames(
                "about-page-title",
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.PRIMARY_50,
                LumoUtility.Padding.Bottom.MEDIUM
        );
        //ajouter une ligne de séparation
        HorizontalLayout separator1 = new HorizontalLayout();
        separator1.setWidth("100%");
        separator1.setHeight("2px");
        separator1.getStyle().set("background-color", "lightgray");
        separator1.addClassNames("separator");

        // Description du site
        Paragraph description = new Paragraph(
                "Ce site est une plateforme de gestion de contenu développée dans le cadre d'un projet de fin d'études. "
                + "Il permet aux utilisateurs de partager des articles, de commenter et d'interagir avec le contenu. "
                + "La plateforme est construite avec des technologies modernes et offre une expérience utilisateur intuitive."
        );
        description.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.Margin.Bottom.XLARGE,
                LumoUtility.Padding.Horizontal.LARGE
        );

        // Section Fonctionnalités
        VerticalLayout featuresSection = createSection("Fonctionnalités principales");
        Paragraph features = new Paragraph();
        features.getElement().setProperty("innerHTML",
                "• Publication d'articles<br>"
                + "• Système de commentaires<br>"
                + "• Catégorisation du contenu<br>"
                + "• Interface responsive<br>"
                + "• Authentification sécurisée"
        );
        features.getStyle().set("text-align", "left");
        featuresSection.add(features);

        // Section Technologies
        VerticalLayout techSection = createSection("Technologies utilisées");
        Paragraph technologies = new Paragraph();
        technologies.getElement().setProperty("innerHTML",
                "• Java Spring Boot<br>"
                + "• Vaadin Framework<br>"
                + "• PostgreSQL<br>"
                + "• Keycloak<br>"
                + "• HTML/CSS/JavaScript"
        );
        technologies.getStyle().set("text-align", "left");
        techSection.add(technologies);

        // Ajout des sections au conteneur principal
        mainContainer.add(title);
        mainContainer.add(separator1);
        mainContainer.add(description);
        mainContainer.add(featuresSection);
        mainContainer.add(techSection);
        add(mainContainer);
    }

    private VerticalLayout createSection(String title) {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setSpacing(true);
        section.addClassNames(
                LumoUtility.Margin.Bottom.XLARGE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM
        );

        H2 sectionTitle = new H2(title);
        sectionTitle.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        section.add(sectionTitle);
        return section;
    }
}
