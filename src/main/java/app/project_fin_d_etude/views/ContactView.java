package app.project_fin_d_etude.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.presenter.MessagePresenter;

/**
 * Vue de contact permettant aux utilisateurs d'envoyer des messages. Cette vue
 * est accessible via la route "/contact".
 */
@Route(value = "contact", layout = MainLayout.class)
@PageTitle("Contact")
public class ContactView extends VerticalLayout {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final int NOTIFICATION_DURATION = 3000;
    private static final String SUCCESS_MESSAGE = "Message envoyé avec succès !";
    private static final String ERROR_MESSAGE = "Erreur lors de l'envoi du message : ";

    private final MessagePresenter messagePresenter;
    private TextField nameField;
    private TextField emailField;
    private TextField subjectField;
    private TextArea messageArea;

    @Autowired
    public ContactView(MessagePresenter messagePresenter) {
        this.messagePresenter = messagePresenter;
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
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL
        );

        // Premier séparateur (au-dessus du titre)
        HorizontalLayout separatorTop = new HorizontalLayout();
        separatorTop.setWidth("70%");
        separatorTop.setHeight("2px");
        separatorTop.getStyle().set("background-color", "lightgray");
        separatorTop.addClassNames("separator");
        mainContent.add(separatorTop);

        mainContent.add(createPageTitle());

        // Deuxième séparateur (en-dessous du titre)
        HorizontalLayout separatorBottom = new HorizontalLayout();
        separatorBottom.setWidth("70%");
        separatorBottom.setHeight("2px");
        separatorBottom.getStyle().set("background-color", "lightgray");
        separatorBottom.addClassNames("separator");
        mainContent.add(separatorBottom);

        mainContent.add(createPersonalInfo());
        mainContent.add(createContactForm());
        return mainContent;
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("CONTACT");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private VerticalLayout createPersonalInfo() {
        VerticalLayout personalInfo = new VerticalLayout();
        personalInfo.setWidth("50%");
        personalInfo.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Margin.Bottom.LARGE
        );
        personalInfo.setSpacing(true);
        personalInfo.setAlignItems(FlexComponent.Alignment.CENTER);

        // Nom
        H2 name = new H2("Amar Wade");
        name.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.BOLD);

        // Rôle
        Paragraph role = new Paragraph("Développeur Junior");
        role.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.SECONDARY);

        // Liens sociaux
        HorizontalLayout socialLinks = new HorizontalLayout();
        socialLinks.setSpacing(true);
        socialLinks.addClassNames(LumoUtility.Margin.Top.MEDIUM);

        // LinkedIn
        Anchor linkedin = new Anchor("https://www.linkedin.com/in/amar-wade/", "LinkedIn");
        linkedin.addClassNames(LumoUtility.TextColor.PRIMARY);

        // Github
        Anchor github = new Anchor("https://github.com/amarwade", "Github");
        github.addClassNames(LumoUtility.TextColor.PRIMARY);

        // Adresse
        Anchor adresse = new Anchor("https://maps.app.goo.gl/nn9wd74L5pT52QK68", "Adresse");
        adresse.addClassNames(LumoUtility.TextColor.PRIMARY);

        socialLinks.add(linkedin, github, adresse);

        return personalInfo;
    }

    private VerticalLayout createContactForm() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth("50%");
        formLayout.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM
        );
        formLayout.setSpacing(true);

        initializeFormFields();
        formLayout.add(nameField, emailField, subjectField, messageArea);
        formLayout.add(createSendButton());

        return formLayout;
    }

    private void initializeFormFields() {
        nameField = createTextField("Nom", "Votre nom");
        emailField = createTextField("Email", "Votre email");
        subjectField = createTextField("Sujet", "Sujet de votre message");
        messageArea = createTextArea();
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

    private TextArea createTextArea() {
        TextArea area = new TextArea("Message");
        area.setPlaceholder("Votre message");
        area.setWidthFull();
        area.setHeight("200px");
        area.setRequired(true);
        area.setRequiredIndicatorVisible(true);
        area.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.TextColor.PRIMARY
        );
        return area;
    }

    private Button createSendButton() {
        Button sendButton = new Button("ENVOYER");
        sendButton.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.AUTO
        );
        sendButton.addClickListener(e -> envoyerMessage());
        return sendButton;
    }

    private void envoyerMessage() {
        if (!validerFormulaire()) {
            return;
        }

        Message message = new Message();
        message.setNom(nameField.getValue());
        message.setEmail(emailField.getValue());
        message.setSujet(subjectField.getValue());
        message.setContenu(messageArea.getValue());

        try {
            messagePresenter.envoyerMessage(message);
            afficherNotification(SUCCESS_MESSAGE, false);
            viderFormulaire();
        } catch (Exception e) {
            afficherNotification(ERROR_MESSAGE + e.getMessage(), true);
        }
    }

    private boolean validerFormulaire() {
        if (!validerChamp(nameField, "Veuillez entrer votre nom")) {
            return false;
        }

        if (!validerChamp(emailField, "Veuillez entrer votre email")) {
            return false;
        }

        if (!validerEmail(emailField.getValue())) {
            afficherNotification("Veuillez entrer une adresse email valide", true);
            return false;
        }

        if (!validerChamp(subjectField, "Veuillez entrer un sujet")) {
            return false;
        }

        if (!validerChamp(messageArea, "Veuillez entrer un message")) {
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

    private boolean validerEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private void afficherNotification(String message, boolean isError) {
        Notification notification = Notification.show(message, NOTIFICATION_DURATION, Notification.Position.TOP_CENTER);
        if (isError) {
            notification.addThemeName("error");
        }
    }

    private void viderFormulaire() {
        nameField.clear();
        emailField.clear();
        subjectField.clear();
        messageArea.clear();
    }
}
