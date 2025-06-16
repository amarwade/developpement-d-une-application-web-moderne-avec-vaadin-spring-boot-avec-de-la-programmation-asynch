package app.project_fin_d_etude.views;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import app.project_fin_d_etude.components.BlogPostCard;
import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

/**
 * Vue principale de l'application affichant la page d'accueil. Cette vue
 * présente les articles récents et populaires.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Accueil")
public class HomePageView extends VerticalLayout implements PostPresenter.PostView {

    private static final int MAX_ARTICLES = 6;
    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final String GRID_TEMPLATE = "repeat(auto-fit, minmax(300px, 1fr))";
    private static final String MAIN_DESCRIPTION = "L'objectif du site est de fournir un espace clair et structuré pour le partage de connaissances, "
            + "d'opinions ou d'actualités tout en assurant la modération et la fiabilité des échanges.";

    private final PostPresenter postPresenter;
    private final DateTimeFormatter dateFormatter;
    private Div recentPostsGrid;
    private Div topPostsGrid;

    @Autowired
    public HomePageView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        this.postPresenter.setView(this);
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        configureLayout();
        add(createMainSection());
        add(createRecentPostsSection());
        add(createTopPostsSection());

        chargerArticles();
    }

    private void configureLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
    }

    private VerticalLayout createMainSection() {
        VerticalLayout mainSection = createSection("100%", FlexComponent.Alignment.CENTER);
        mainSection.addClassNames(
                LumoUtility.Padding.Vertical.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        // Premier séparateur (au-dessus du titre)
        HorizontalLayout separatorTop = new HorizontalLayout();
        separatorTop.setWidth("80%");
        separatorTop.setHeight("2px");
        separatorTop.getStyle().set("background-color", "lightgray");
        separatorTop.addClassNames("separator");
        mainSection.add(separatorTop);

        mainSection.add(createMainTitle());

        // Deuxième séparateur (en-dessous du titre)
        HorizontalLayout separatorBottom = new HorizontalLayout();
        separatorBottom.setWidth("80%");
        separatorBottom.setHeight("2px");
        separatorBottom.getStyle().set("background-color", "lightgray");
        separatorBottom.addClassNames("separator");
        mainSection.add(separatorBottom);

        mainSection.add(createMainDescription());

        return mainSection;
    }

    private H1 createMainTitle() {
        H1 title = new H1("LE BLOG");
        title.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.FontWeight.BOLD
        );
        return title;
    }

    private Paragraph createMainDescription() {
        Paragraph description = new Paragraph(MAIN_DESCRIPTION);
        description.addClassNames(
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.FontSize.LARGE,
                "max-w-md",
                LumoUtility.Margin.AUTO
        );
        return description;
    }

    private VerticalLayout createRecentPostsSection() {
        VerticalLayout section = createSection("100%", FlexComponent.Alignment.START);
        section.addClassNames(
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Padding.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        section.add(createSectionTitle("Articles récents"));
        recentPostsGrid = createPostsGrid();
        section.add(recentPostsGrid);

        return section;
    }

    private VerticalLayout createTopPostsSection() {
        VerticalLayout section = createSection("100%", FlexComponent.Alignment.START);
        section.addClassNames(
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Padding.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );

        section.add(createSectionTitle("Articles populaires"));
        topPostsGrid = createPostsGrid();
        section.add(topPostsGrid);

        return section;
    }

    private VerticalLayout createSection(String width, FlexComponent.Alignment alignment) {
        VerticalLayout section = new VerticalLayout();
        section.setWidth(width);
        section.setAlignItems(alignment);
        section.addClassNames(LumoUtility.Margin.AUTO, LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.LARGE, LumoUtility.BoxShadow.SMALL, LumoUtility.Padding.MEDIUM);
        section.getStyle().setWidth("100%");
        return section;
    }

    private H2 createSectionTitle(String title) {
        H2 sectionTitle = new H2(title);
        sectionTitle.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.PRIMARY_50,
                LumoUtility.Padding.Bottom.SMALL,
                LumoUtility.Margin.Bottom.MEDIUM
        );
        return sectionTitle;
    }

    private Div createPostsGrid() {
        Div grid = new Div();
        grid.addClassNames(LumoUtility.Display.GRID, LumoUtility.Gap.MEDIUM);
        grid.getStyle().set("grid-template-columns", GRID_TEMPLATE);
        return grid;
    }

    private void chargerArticles() {
        VaadinUtils.showLoading(this);
        postPresenter.chargerPosts();
    }

    @Override
    public void afficherPosts(List<Post> posts) {
        VaadinUtils.hideLoading(this);
        afficherArticlesRecents(posts);
        afficherArticlesPopulaires(posts);
    }

    @Override
    public void afficherCategories(List<app.project_fin_d_etude.model.CategoriePost> categories) {
        // Non utilisé dans cette vue
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

    private void afficherArticlesRecents(List<Post> articles) {
        recentPostsGrid.removeAll();
        articles.stream()
                .limit(MAX_ARTICLES)
                .forEach(post -> {
                    BlogPostCard card = createPostCard(post);
                    VaadinUtils.addResponsiveClass(card);
                    recentPostsGrid.add(card);
                });
    }

    private void afficherArticlesPopulaires(List<Post> articles) {
        topPostsGrid.removeAll();
        articles.stream()
                .limit(MAX_ARTICLES)
                .forEach(post -> {
                    BlogPostCard card = createPostCard(post);
                    VaadinUtils.addResponsiveClass(card);
                    topPostsGrid.add(card);
                });
    }

    private BlogPostCard createPostCard(Post post) {
        BlogPostCard card = new BlogPostCard(
                post.getTitre(),
                post.getDatePublication().format(dateFormatter),
                post.getContenu()
        );
        card.addClickListener(e -> {
            VaadinUtils.showLoading(this);
            getUI().ifPresent(ui -> ui.navigate("user/article/" + post.getId()));
        });
        return card;
    }
}
