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
import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.presenter.CommentairePresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

@Route(value = "admin/commentaires", layout = AdminLayout.class)
@PageTitle("Gestion des commentaires - Administration")
public class AdminCommentairesView extends VerticalLayout implements CommentairePresenter.CommentaireView {

    private final CommentairePresenter commentairePresenter;
    private final Grid<Commentaire> grid = new Grid<>(Commentaire.class);

    @Autowired
    public AdminCommentairesView(CommentairePresenter commentairePresenter) {
        this.commentairePresenter = commentairePresenter;
        this.commentairePresenter.setView(this);

        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);

        add(createMainContent());
        configureGrid();
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

    private H1 createPageTitle() {
        H1 pageTitle = new H1("GESTION DES COMMENTAIRES");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
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
        grid.addClassNames("contact-grid");
        grid.setColumns("id", "contenu", "dateCreation");

        grid.addColumn(commentaire -> {
            if (commentaire.getAuteur() != null) {
                return commentaire.getAuteur().getNom();
            }
            return "Auteur inconnu";
        }).setHeader("Auteur");

        grid.addColumn(commentaire -> {
            if (commentaire.getPost() != null) {
                return commentaire.getPost().getTitre();
            }
            return "Article inconnu";
        }).setHeader("Article");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    @Override
    public void afficherCommentaires(List<Commentaire> commentaires) {
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(commentaires)));
    }

    @Override
    public void afficherMessage(String message) {
        VaadinUtils.showSuccessNotification(message);
    }

    @Override
    public void afficherErreur(String erreur) {
        VaadinUtils.showErrorNotification(erreur);
    }

    @Override
    public void rafraichirListe() {
        // Optionnel : recharger les commentaires si besoin
    }
}
