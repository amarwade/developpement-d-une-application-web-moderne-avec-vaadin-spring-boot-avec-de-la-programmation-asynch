package app.project_fin_d_etude.views;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.model.CategoriePost;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

/**
 * Vue principale affichant la liste des articles avec pagination et recherche.
 * Cette vue est accessible via la route "/articles".
 */
@Route(value = "articles", layout = MainLayout.class)
@PageTitle("Articles")
public class ArticlesView extends VerticalLayout implements PostPresenter.PostView {

    private static final int ITEMS_PER_PAGE = 6;
    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final String GRID_TEMPLATE_COLUMNS = "repeat(auto-fit, minmax(300px, 1fr))";
    private static final int PREVIEW_LENGTH = 100;

    private final PostPresenter postPresenter;
    private TextField searchField;
    private Div recentPostsGrid;
    private Div topPostsGrid;
    private HorizontalLayout pagination;
    private final int currentPage = 1;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Autowired
    public ArticlesView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        configureLayout();
        add(createMainContent());
        chargerArticles();
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
        separatorTop.setWidth("80%");
        separatorTop.setHeight("2px");
        separatorTop.getStyle().set("background-color", "lightgray");
        separatorTop.addClassNames("separator");
        mainContent.add(separatorTop);

        mainContent.add(createPageTitle());

        // Deuxième séparateur (en-dessous du titre)
        HorizontalLayout separatorBottom = new HorizontalLayout();
        separatorBottom.setWidth("80%");
        separatorBottom.setHeight("2px");
        separatorBottom.getStyle().set("background-color", "lightgray");
        separatorBottom.addClassNames("separator");
        mainContent.add(separatorBottom);

        mainContent.add(createSearchAndFilterSection());
        mainContent.add(createRecentPostsSection());
        mainContent.add(createTopPostsSection());
        mainContent.add(createPagination());

        return mainContent;
    }

    private H1 createPageTitle() {
        H1 pageTitle = new H1("ARTICLES");
        pageTitle.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.FontWeight.BOLD
        );
        return pageTitle;
    }

    private HorizontalLayout createSearchAndFilterSection() {
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidth("35%");
        searchLayout.setSpacing(false);
        searchLayout.setAlignItems(FlexComponent.Alignment.END);
        searchLayout.addClassNames(
                LumoUtility.Margin.AUTO,
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Padding.LARGE,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        searchField = createSearchField();
        Button searchButton = createSearchButton();

        searchLayout.add(searchField, searchButton);
        searchLayout.expand(searchField);
        searchLayout.getStyle().setWidth("35%");
        return searchLayout;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Article's name or category");
        searchField.setWidthFull();
        searchField.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        searchField.addValueChangeListener(e -> postPresenter.rechercherArticles(searchField.getValue()));
        searchField.getStyle().setFlexGrow(null).setWidth("50%");
        return searchField;
    }

    private Button createSearchButton() {
        Button searchButton = VaadinUtils.createPrimaryButton("RECHERCHER");
        searchButton.addClickListener(e -> {
            VaadinUtils.showLoading(this);
            postPresenter.rechercherArticles(searchField.getValue());
        });
        return searchButton;
    }

    private VerticalLayout createRecentPostsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setAlignItems(FlexComponent.Alignment.START);
        section.addClassNames(LumoUtility.Margin.Bottom.LARGE);

        H2 title = createSectionTitle("Recent blog posts");
        section.add(title);

        recentPostsGrid = createPostsGrid();
        section.add(recentPostsGrid);

        return section;
    }

    private VerticalLayout createTopPostsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setAlignItems(FlexComponent.Alignment.START);
        section.addClassNames(LumoUtility.Margin.Bottom.LARGE);

        H2 title = createSectionTitle("Top posts");
        section.add(title);

        topPostsGrid = createPostsGrid();
        section.add(topPostsGrid);

        return section;
    }

    private H2 createSectionTitle(String text) {
        H2 title = new H2(text);
        title.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.PRIMARY_50,
                LumoUtility.Padding.Bottom.SMALL
        );
        return title;
    }

    private Div createPostsGrid() {
        Div grid = new Div();
        grid.addClassNames(LumoUtility.Display.GRID, LumoUtility.Gap.MEDIUM);
        grid.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        return grid;
    }

    private HorizontalLayout createPagination() {
        pagination = new HorizontalLayout();
        pagination.addClassNames(
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.Bottom.LARGE,
                LumoUtility.Gap.SMALL
        );
        pagination.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return pagination;
    }

    private void chargerArticles() {
        VaadinUtils.showLoading(this);
        postPresenter.chargerPosts();
    }

    @Override
    public void afficherPosts(List<Post> posts) {
        VaadinUtils.hideLoading(this);
        recentPostsGrid.removeAll();
        topPostsGrid.removeAll();

        if (posts.isEmpty()) {
            recentPostsGrid.add(new Paragraph("Aucun article trouvé."));
            return;
        }

        // Afficher les articles récents
        H2 recentPostsTitle = createSectionTitle("Recent blog posts");
        recentPostsGrid.add(recentPostsTitle);

        Div recentArticlesLayout = new Div();
        recentArticlesLayout.addClassNames(LumoUtility.Display.GRID, LumoUtility.Gap.MEDIUM);
        recentArticlesLayout.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        VaadinUtils.addResponsiveClass(recentArticlesLayout);

        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, posts.size());
        for (int i = startIndex; i < endIndex; i++) {
            Post article = posts.get(i);
            Div card = createBlogPostCard(article);
            VaadinUtils.addResponsiveClass(card);
            recentArticlesLayout.add(card);
        }
        recentPostsGrid.add(recentArticlesLayout);

        // Afficher les top posts
        H2 topPostsTitle = createSectionTitle("Top posts");
        topPostsGrid.add(topPostsTitle);

        Div topArticlesLayout = new Div();
        topArticlesLayout.addClassNames(LumoUtility.Display.GRID, LumoUtility.Gap.MEDIUM);
        topArticlesLayout.getStyle().set("grid-template-columns", GRID_TEMPLATE_COLUMNS);
        VaadinUtils.addResponsiveClass(topArticlesLayout);

        posts.stream()
                .limit(6)
                .forEach(post -> {
                    Div card = createBlogPostCard(post);
                    VaadinUtils.addResponsiveClass(card);
                    topArticlesLayout.add(card);
                });
        topPostsGrid.add(topArticlesLayout);

        mettreAJourPagination(posts.size());
    }

    private Div createBlogPostCard(Post post) {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST,
                "animate__animated",
                "animate__fadeIn"
        );
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "space-between");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("transition", "transform 0.2s ease-in-out");

        card.addClickListener(e -> {
            VaadinUtils.showLoading(this);
            getUI().ifPresent(ui -> ui.navigate("user/article/" + post.getId()));
        });

        card.getElement().addEventListener("mouseenter", e -> card.getStyle().set("transform", "translateY(-5px)"));
        card.getElement().addEventListener("mouseleave", e -> card.getStyle().set("transform", "translateY(0)"));

        Div contentDiv = new Div();
        contentDiv.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        H3 title = new H3(post.getTitre());
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Bottom.SMALL, LumoUtility.TextColor.PRIMARY);

        Paragraph date = new Paragraph(post.getDatePublication().format(dateFormatter));
        date.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Bottom.SMALL);

        Paragraph preview = new Paragraph(post.getContenu().substring(0, Math.min(post.getContenu().length(), PREVIEW_LENGTH)) + "...");
        preview.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.TextColor.PRIMARY, LumoUtility.Margin.Bottom.MEDIUM);

        contentDiv.add(title, date, preview);

        Button readMoreButton = VaadinUtils.createPrimaryButton("Lire plus");
        readMoreButton.addClickListener(e -> {
            VaadinUtils.showLoading(this);
            getUI().ifPresent(ui -> ui.navigate("user/article/" + post.getId()));
        });

        card.add(contentDiv, readMoreButton);
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

    @Override
    public void afficherPost(Post post) {
        // Non utilisé dans cette vue
    }

    @Override
    public void afficherCategories(List<CategoriePost> categories) {
        // Non utilisé dans cette vue
    }

    @Override
    public void redirigerVersDetail(Long postId) {
        getUI().ifPresent(ui -> ui.navigate("user/article/" + postId));
    }

    @Override
    public void viderFormulaire() {
        // Non utilisé dans cette vue
    }

    @Override
    public void mettreAJourPagination(int totalItems) {
        pagination.removeAll();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.add(createPaginationControls(totalPages));
    }

    private HorizontalLayout createPaginationControls(int totalPages) {
        HorizontalLayout paginationLayout = new HorizontalLayout();
        paginationLayout.setSpacing(true);
        paginationLayout.addClassNames(
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.JustifyContent.CENTER
        );

        Button prevButton = new Button("← Previous");
        prevButton.addClickListener(e -> postPresenter.loadPaginatedPosts(currentPage - 2, ITEMS_PER_PAGE));
        prevButton.setEnabled(currentPage > 1);
        prevButton.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        paginationLayout.add(prevButton);

        for (int i = 0; i < totalPages; i++) {
            int pageIndex = i;
            Button pageButton = new Button(String.valueOf(i + 1));
            pageButton.addClickListener(e -> postPresenter.loadPaginatedPosts(pageIndex, ITEMS_PER_PAGE));
            pageButton.addClassNames(
                    LumoUtility.Background.CONTRAST_5,
                    LumoUtility.TextColor.PRIMARY,
                    LumoUtility.BorderRadius.MEDIUM,
                    LumoUtility.Border.ALL,
                    LumoUtility.BorderColor.CONTRAST
            );
            if (i == currentPage - 1) {
                pageButton.addClassNames(LumoUtility.Background.PRIMARY, LumoUtility.TextColor.PRIMARY_CONTRAST);
            }
            paginationLayout.add(pageButton);
        }

        Button nextButton = new Button("Next →");
        nextButton.addClickListener(e -> postPresenter.loadPaginatedPosts(currentPage, ITEMS_PER_PAGE));
        nextButton.setEnabled(currentPage < totalPages);
        nextButton.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        paginationLayout.add(nextButton);

        return paginationLayout;
    }
}
