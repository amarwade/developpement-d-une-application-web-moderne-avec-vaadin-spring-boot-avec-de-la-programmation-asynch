package app.project_fin_d_etude.views.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.layout.AdminLayout;
import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.presenter.MessagePresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

@Route(value = "admin/messages", layout = AdminLayout.class)
@PageTitle("Gestion des messages - Administration")
public class AdminMessagesView extends VerticalLayout implements MessagePresenter.MessageView {

    private final MessagePresenter messagePresenter;
    private Grid<Message> grid;

    @Autowired
    public AdminMessagesView(MessagePresenter messagePresenter) {
        this.messagePresenter = messagePresenter;
        this.messagePresenter.setView(this);
        configureGrid();
        add(grid);
        messagePresenter.chargerMessages();
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("GESTION DES MESSAGES");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
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
        mainContent.add(createContentSection());

        return mainContent;
    }

    private VerticalLayout createContentSection() {
        VerticalLayout contentSection = new VerticalLayout();
        contentSection.setWidth("90%");
        contentSection.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM
        );

        contentSection.add(grid);
        return contentSection;
    }

    private void configureGrid() {
        grid = new Grid<>(Message.class, false);
        grid.addColumn(Message::getId).setHeader("Id");
        grid.addColumn(Message::getNom).setHeader("Nom");
        grid.addColumn(Message::getEmail).setHeader("Email");
        grid.addColumn(Message::getSujet).setHeader("Sujet");
        grid.addColumn(Message::getContenu).setHeader("Contenu");
        grid.addColumn(Message::getDateEnvoi).setHeader("Date Envoi");
        grid.addColumn(Message::isLu).setHeader("Lu");
    }

    @Override
    public void afficherMessages(List<Message> messages) {
        grid.setItems(messages);
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
