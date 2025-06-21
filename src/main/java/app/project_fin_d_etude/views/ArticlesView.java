package app.project_fin_d_etude.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import app.project_fin_d_etude.components.BlogPostCard;
import app.project_fin_d_etude.layout.MainLayout;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.presenter.PostPresenter;
import app.project_fin_d_etude.utils.VaadinUtils;

/**
 * Vue affichant la liste des articles (posts) avec pagination. Les posts sont
 * affichés automatiquement après chargement.
 */
@Route(value = "articles", layout = MainLayout.class)
@PageTitle("Articles")
public class ArticlesView extends VerticalLayout implements PostPresenter.PostView {

    private static final int ITEMS_PER_PAGE = 6;
    private final PostPresenter postPresenter;
    private int currentPage = 0;
    private final VerticalLayout postsContainer = new VerticalLayout();
    private HorizontalLayout pagination;
    private String currentKeyword = null;
    private VerticalLayout loader;

    /**
     * Constructeur de la vue Articles. Les posts sont chargés et affichés
     * automatiquement à l'initialisation.
     */
    @Autowired
    public ArticlesView(PostPresenter postPresenter) {
        this.postPresenter = postPresenter;
        this.postPresenter.setView(this);
        configureLayout();

        add(createMainSection());
        add(createSearchBar());

        postsContainer.setVisible(false);
        add(postsContainer);

        pagination = new HorizontalLayout();
        pagination.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        pagination.setWidthFull();
        pagination.setVisible(false);
        add(pagination);

        showLoader();
        postPresenter.chargerPosts();

    }

    private VerticalLayout createMainSection() {
        final VerticalLayout mainSection = VaadinUtils.createSection("100%", FlexComponent.Alignment.CENTER);
        mainSection.addClassNames(
                LumoUtility.Padding.Vertical.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST
        );
        mainSection.add(VaadinUtils.createSeparator("80%"));
        mainSection.add(createMainTitle());
        mainSection.add(VaadinUtils.createSeparator("80%"));
        return mainSection;
    }

    private H1 createMainTitle() {
        final H1 title = new H1("ARTICLES");
        title.addClassNames(
                LumoUtility.FontSize.XXXLARGE,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.FontWeight.BOLD
        );
        return title;
    }

    /**
     * Configure le layout principal de la vue.
     */
    private void configureLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassNames(LumoUtility.Background.CONTRAST_5);
        postsContainer.setWidthFull();
        postsContainer.setSpacing(true);
        postsContainer.setPadding(true);
    }

    /**
     * Crée la barre de recherche d'articles.
     */
    private HorizontalLayout createSearchBar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Rechercher un article...");
        searchField.setWidth("350px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        Button searchButton = new Button("Rechercher", e -> triggerSearch(searchField.getValue()));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchField.addValueChangeListener(e -> {
            String value = e.getValue();
            currentPage = 1;
            showLoader();
            if (value == null || value.trim().isEmpty()) {
                currentKeyword = null;
                postPresenter.loadPaginatedPosts(0, ITEMS_PER_PAGE);
            } else {
                currentKeyword = value.trim();
                postPresenter.rechercherArticles(currentKeyword, 0, ITEMS_PER_PAGE);
            }
        });
        HorizontalLayout searchBar = new HorizontalLayout(searchField, searchButton);
        searchBar.setAlignItems(Alignment.CENTER);
        searchBar.setWidthFull();
        return searchBar;
    }

    /**
     * Déclenche la recherche d'articles par mot-clé.
     */
    private void triggerSearch(String keyword) {
        currentPage = 1;
        showLoader();
        if (keyword == null || keyword.trim().isEmpty()) {
            currentKeyword = null;
            postPresenter.loadPaginatedPosts(0, ITEMS_PER_PAGE);
        } else {
            currentKeyword = keyword.trim();
            postPresenter.rechercherArticles(currentKeyword, 0, ITEMS_PER_PAGE);
        }
    }

    /**
     * Affiche la liste des posts dans le conteneur principal. Appelée
     * automatiquement après chargement des posts.
     */
    @Override
    public void afficherPosts(List<Post> posts) {
        hideLoader();
        postsContainer.removeAll();
        if (posts == null || posts.isEmpty()) {
            Paragraph emptyMsg = new Paragraph("Aucun article trouvé.");
            emptyMsg.addClassNames(
                    LumoUtility.TextColor.SECONDARY,
                    LumoUtility.TextAlignment.CENTER,
                    LumoUtility.FontSize.LARGE,
                    LumoUtility.Margin.Top.XLARGE
            );
            postsContainer.add(emptyMsg);
        } else {
            for (Post post : posts) {
                postsContainer.add(new BlogPostCard(post, p -> getUI().ifPresent(ui -> ui.navigate("user/article/" + p.getId()))));
            }
        }
        mettreAJourPagination(posts != null ? posts.size() : 0);
    }

    /**
     * Met à jour la pagination en fonction du nombre total d'éléments.
     */
    @Override
    public void mettreAJourPagination(int totalItems) {
        if (pagination == null) {
            pagination = new HorizontalLayout();
            pagination.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            pagination.setWidthFull();
            add(pagination);
        }
        pagination.removeAll();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages <= 1) {
            return;
        }

        Button prevButton = new Button("← Précédent");
        prevButton.setEnabled(currentPage > 1);
        prevButton.addClickListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                triggerPaginatedSearch();
            }
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
                triggerPaginatedSearch();
            });
            pageNumbers.add(pageBtn);
        }

        Button nextButton = new Button("Suivant →");
        nextButton.setEnabled(currentPage < totalPages);
        nextButton.addClickListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                triggerPaginatedSearch();
            }
        });

        pagination.add(prevButton, pageNumbers, nextButton);
    }

    /**
     * Déclenche la recherche paginée selon le contexte (mot-clé ou non).
     */
    private void triggerPaginatedSearch() {
        int pageIndex = currentPage - 1;
        showLoader();
        if (currentKeyword != null && !currentKeyword.trim().isEmpty()) {
            postPresenter.rechercherArticles(currentKeyword, pageIndex, ITEMS_PER_PAGE);
        } else {
            postPresenter.loadPaginatedPosts(pageIndex, ITEMS_PER_PAGE);
        }
    }

    private void showLoader() {
        if (loader == null) {
            loader = new VerticalLayout();
            loader.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            loader.setAlignItems(Alignment.CENTER);
            loader.getStyle().set("padding", "50px");
            ProgressBar progress = new ProgressBar();
            progress.setIndeterminate(true);
            loader.add(new H3("Chargement des articles..."), progress);
        }
        add(loader);
        postsContainer.setVisible(false);
        pagination.setVisible(false);
    }

    private void hideLoader() {
        if (loader != null) {
            remove(loader);
        }
        postsContainer.setVisible(true);
        pagination.setVisible(true);
    }

    // ... autres méthodes d'interface non utilisées
    @Override
    public void afficherPost(Post post) {
    }

    @Override
    public void viderFormulaire() {
    }

    @Override
    public void afficherMessage(String message) {
    }

    @Override
    public void afficherErreur(String erreur) {
    }

    @Override
    public void redirigerVersDetail(Long postId) {
    }
}
