package app.project_fin_d_etude.utils;

public class TextUtils {

    /**
     * Tronque un texte en ajoutant des points de suspension s’il dépasse une certaine longueur.
     */
    public static String resume(String texte, int maxLength) {
        if (texte == null) return "";
        if (texte.length() <= maxLength) return texte;
        return texte.substring(0, maxLength).trim() + "...";
    }

    /**
     * Nettoie les espaces en trop et supprime les sauts de ligne.
     */
    public static String clean(String texte) {
        return texte == null ? "" : texte.replaceAll("\\s+", " ").trim();
    }
}
