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
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

@Route(value = "admin/posts", layout = AdminLayout.class)
@PageTitle("Gestion des articles - Administration")
public class AdminPostsView extends VerticalLayout implements PostPresenter.PostView {

    private final PostPresenter postPresenter;
    private final Grid<Post> grid = new Grid<>(Post.class);

    @Autowired
    public AdminPostsView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        this.postPresenter.setView(this);

        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);

        add(createMainContent());
        configureGrid();
        loadPosts();
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
        H1 pageTitle = new H1("GESTION DES ARTICLES");
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
        grid.setColumns("id", "titre", "contenu", "datePublication");

        grid.addColumn(post -> {
            if (post.getAuteur() != null) {
                return post.getAuteur().getNom();
            }
            return "Auteur inconnu";
        }).setHeader("Auteur");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void loadPosts() {
        postPresenter.chargerPosts();
    }

    @Override
    public void afficherPosts(List<Post> posts) {
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(posts)));
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
    public void viderFormulaire() {
        // Non utilisé dans cette vue
    }

    @Override
    public void redirigerVersDetail(Long postId) {
        // Non utilisé dans cette vue
    }

    @Override
    public void mettreAJourPagination(int totalItems) {
        // Non utilisé dans cette vue
    }

    @Override
    public void afficherPost(Post post) {
        // Non utilisé dans cette vue
    }
}
