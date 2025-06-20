package app.project_fin_d_etude.views;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.components.BlogPostCard;
import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

@Route(value = "articles", layout = MainLayout.class)
@PageTitle("Articles")
public class ArticlesView extends VerticalLayout implements PostPresenter.PostView {

    private static final int ITEMS_PER_PAGE = 6;
    private static final String GRID_TEMPLATE_COLUMNS = "repeat(auto-fit, minmax(300px, 1fr))";

    private final PostPresenter postPresenter;
    private TextField searchField;
    private Div recentPostsGrid;
    private Div topPostsGrid;
    private HorizontalLayout pagination;
    private int currentPage = 1;
    private List<Post> allPosts = new ArrayList<>();

    @Autowired
    public ArticlesView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        this.postPresenter.setView(this);
        configureLayout();
        add(createMainContent());
        loadInitialPosts();
    }

    private void configureLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContent = VaadinUtils.createSection("100%", FlexComponent.Alignment.CENTER);
        mainContent.add(
                VaadinUtils.createSeparator("80%"),
                VaadinUtils.createPageTitle("ARTICLES"),
                VaadinUtils.createSeparator("80%"),
                createSearchAndFilterSection(),
                createRecentPostsSection(),
                createTopPostsSection(),
                createPagination()
        );
        return mainContent;
    }

    private void loadInitialPosts() {
        VaadinUtils.showLoading(this);
        postPresenter.loadPaginatedPosts(0, ITEMS_PER_PAGE);
    }

    @Override
    public void afficherPosts(List<Post> posts) {
        getUI().ifPresent(ui -> ui.access(() -> {
            VaadinUtils.hideLoading(this);
            this.allPosts = posts;
            updatePostsDisplay();
        }));
    }

    private void updatePostsDisplay() {
        recentPostsGrid.removeAll();
        // topPostsGrid.removeAll(); // plus besoin

        if (allPosts.isEmpty()) {
            recentPostsGrid.add(new Paragraph("Aucun article trouvé."));
            return;
        }

        // Articles récents (paginés)
        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allPosts.size());

        Div recentArticlesLayout = createPostsGrid();
        for (int i = startIndex; i < endIndex; i++) {
            recentArticlesLayout.add(createPostCard(allPosts.get(i)));
        }
        recentPostsGrid.add(recentArticlesLayout);

        mettreAJourPagination(allPosts.size());
    }

    private Div createPostCard(Post post) {
        return new BlogPostCard(post, p
                -> getUI().ifPresent(ui -> ui.navigate("user/article/" + p.getId()))
        );
    }

    @Override
    public void mettreAJourPagination(int totalItems) {
        getUI().ifPresent(ui -> ui.access(() -> {
            pagination.removeAll();
            int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
            if (totalPages <= 1) {
                return;
            }

            Button prevButton = new Button("← Précédent");
            prevButton.setEnabled(currentPage > 1);
            prevButton.addClickListener(e -> {
                currentPage--;
                postPresenter.loadPaginatedPosts(currentPage - 1, ITEMS_PER_PAGE);
            });

            HorizontalLayout pageNumbers = new HorizontalLayout();
            pageNumbers.setSpacing(true);

            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, currentPage + 2);

            for (int i = startPage; i <= endPage; i++) {
                Button pageBtn = new Button(String.valueOf(i));
                pageBtn.setEnabled(i != currentPage);
                if (i == currentPage) {
                    pageBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                }
                int page = i;
                pageBtn.addClickListener(e -> {
                    currentPage = page;
                    postPresenter.loadPaginatedPosts(page - 1, ITEMS_PER_PAGE);
                });
                pageNumbers.add(pageBtn);
            }

            Button nextButton = new Button("Suivant →");
            nextButton.setEnabled(currentPage < totalPages);
            nextButton.addClickListener(e -> {
                currentPage++;
                postPresenter.loadPaginatedPosts(currentPage - 1, ITEMS_PER_PAGE);
            });

            pagination.add(prevButton, pageNumbers, nextButton);
        }));
    }

    // ... autres méthodes d'interface non utilisées
    @Override
    public void afficherPost(Post post) {
    }

    @Override
    public void redirigerVersDetail(Long postId) {
    }

    @Override
    public void viderFormulaire() {
    }

    @Override
    public void afficherMessage(String message) {
        VaadinUtils.showSuccessNotification(message);
    }

    @Override
    public void afficherErreur(String erreur) {
        VaadinUtils.showErrorNotification(erreur);
    }

    private VerticalLayout createSearchAndFilterSection() {
        // À adapter selon vos besoins (champ de recherche, filtres, etc.)
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(false);
        section.setSpacing(false);
        return section;
    }

    private Div createRecentPostsSection() {
        recentPostsGrid = new Div();
        recentPostsGrid.setWidthFull();
        recentPostsGrid.getStyle().set("display", "grid");
        recentPostsGrid.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        return recentPostsGrid;
    }

    private Div createTopPostsSection() {
        topPostsGrid = new Div();
        topPostsGrid.setWidthFull();
        topPostsGrid.getStyle().set("display", "grid");
        topPostsGrid.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        return topPostsGrid;
    }

    private HorizontalLayout createPagination() {
        pagination = new HorizontalLayout();
        pagination.setWidthFull();
        pagination.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return pagination;
    }

    private Div createPostsGrid() {
        Div grid = new Div();
        grid.setWidthFull();
        grid.getStyle().set("display", "grid");
        grid.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        grid.getStyle().set("gap", "1rem");
        return grid;
    }
}
