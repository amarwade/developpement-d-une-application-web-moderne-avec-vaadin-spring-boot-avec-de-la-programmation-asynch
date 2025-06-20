package app.project_fin_d_etude.utils;

/**
 * Classe contenant toutes les constantes de routes de l'application. Centralise
 * la gestion des URLs pour éviter les incohérences.
 */
public final class Routes {

    // Routes publiques
    public static final String HOME = "/";
    public static final String ARTICLES = "/articles";
    public static final String ABOUT = "/about";
    public static final String CONTACT = "/contact";

    // Routes utilisateur
    public static final String USER_PROFILE = "/user/profile";
    public static final String USER_INFO = "/user-info";
    public static final String USER_CREATE_POST = "/user/create-post";
    public static final String USER_ARTICLE = "/user/article";

    // Routes d'authentification
    public static final String LOGIN = "/login";

    // Méthodes utilitaires
    public static String getUserArticleUrl(Long postId) {
        return "user/article/" + postId;
    }

    private Routes() {
        // Classe utilitaire, constructeur privé
    }
}
