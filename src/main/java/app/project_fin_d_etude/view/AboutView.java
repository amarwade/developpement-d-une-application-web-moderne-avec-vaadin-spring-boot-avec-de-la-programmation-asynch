package app.project_fin_d_etude.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
@PageTitle("À Propos")
public class AboutView extends VerticalLayout {

    private static final String SITE_DESCRIPTION_1
            = "Ce site est une application web de blog développée dans le cadre d'un projet de stage. "
            + "Il a été conçu pour permettre aux utilisateurs de publier librement des articles, des tutoriels, "
            + "des actualités, ou tout autre contenu lié à leurs expertises.";

    private static final String SITE_DESCRIPTION_2
            = "L'objectif principal est de proposer une plateforme minimale, sécurisée et accessible, "
            + "adaptée à un usage communautaire, tout en mettant en œuvre des technologies modernes comme "
            + "Vaadin, Spring Boot, MySQL, et la programmation asynchrone en Java.";

    private static final String DEVELOPER_DESCRIPTION
            = "Je suis un développeur passionné par la création d'applications web modernes et performantes. "
            + "Mon parcours m'a permis d'acquérir une solide expertise dans le développement Java et les "
            + "technologies web. Je m'efforce constamment d'améliorer mes compétences et de rester à jour "
            + "avec les dernières tendances technologiques.";

    public AboutView() {
        configureLayout();
        add(createMainContent());
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
                LumoUtility.Padding.Vertical.LARGE,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL
        );

        mainContent.add(createPageTitle());
        mainContent.add(createAboutSiteSection());
        mainContent.add(createAboutDeveloperSection());

        return mainContent;
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("À PROPOS");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private VerticalLayout createAboutSiteSection() {
        VerticalLayout section = createSection("À propos du site");
        section.addClassNames(LumoUtility.Margin.Bottom.LARGE);

        Paragraph siteDescription1 = createParagraph(SITE_DESCRIPTION_1);
        Paragraph siteDescription2 = createParagraph(SITE_DESCRIPTION_2);

        H3 siteUsersTitle = createSubTitle("Le site intègre deux types de profils :");
        Paragraph userType1 = createParagraph("• Utilisateurs : peuvent créer, modifier, commenter des articles.");
        Paragraph userType2 = createParagraph("• Administrateurs : peuvent modérer les contenus, suspendre des utilisateurs, et accéder à une interface d'administration dédiée.");

        H3 projectGoalsTitle = createSubTitle("Ce projet est aussi une démonstration pratique de bonnes pratiques de développement logiciel :");
        Paragraph goal1 = createParagraph("• Architecture en couches (Model, Repository, Service, View)");
        Paragraph goal2 = createParagraph("• Design pattern MVP");
        Paragraph goal3 = createParagraph("• Gestion des rôles avec Spring Security");
        Paragraph goal4 = createParagraph("• Déploiement Cloud avec Docker et Google Cloud Run");

        section.add(siteDescription1, siteDescription2, siteUsersTitle, userType1, userType2,
                projectGoalsTitle, goal1, goal2, goal3, goal4);
        return section;
    }

    private VerticalLayout createAboutDeveloperSection() {
        VerticalLayout section = createSection("À propos du développeur");
        section.addClassNames(LumoUtility.Margin.Top.LARGE);

        Paragraph developerDescription = createParagraph(DEVELOPER_DESCRIPTION);

        H3 skillsTitle = createSubTitle("Compétences :");
        Paragraph skill1 = createParagraph("• Développement Java et Spring Boot");
        Paragraph skill2 = createParagraph("• Frameworks web (Vaadin, React)");
        Paragraph skill3 = createParagraph("• Bases de données (MySQL, PostgreSQL)");
        Paragraph skill4 = createParagraph("• DevOps et déploiement cloud");
        Paragraph skill5 = createParagraph("• Architecture logicielle et design patterns");
        Paragraph skill6 = createParagraph("• Tests unitaires et d'intégration");
        Paragraph skill7 = createParagraph("• Gestion de version (Git)");

        H3 educationTitle = createSubTitle("Formation :");
        Paragraph education1 = createParagraph("• Master en Informatique");
        Paragraph education2 = createParagraph("• Certifications Java et Spring");

        section.add(developerDescription, skillsTitle, skill1, skill2, skill3, skill4,
                skill5, skill6, skill7, educationTitle, education1, education2);
        return section;
    }

    private VerticalLayout createSection(String title) {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(true);
        section.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.BorderRadius.MEDIUM);

        H2 sectionTitle = new H2(title);
        sectionTitle.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.PRIMARY_50,
                LumoUtility.Padding.Bottom.SMALL,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        section.add(sectionTitle);
        return section;
    }

    private H3 createSubTitle(String text) {
        H3 subTitle = new H3(text);
        subTitle.addClassNames(
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.Top.LARGE
        );
        return subTitle;
    }

    private Paragraph createParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.FontSize.MEDIUM);
        return paragraph;
    }
}
