package app.project_fin_d_etude.utils;

import app.project_fin_d_etude.model.Commentaire;
import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.model.Post;
import app.project_fin_d_etude.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de validation pour les entités métier. Fournit des méthodes de
 * validation spécifiques aux entités.
 */
public final class EntityValidator {

    private EntityValidator() {
        // Classe utilitaire, constructeur privé
    }

    /**
     * Valide une entité Post
     */
    public static ValidationResult validatePost(Post post) {
        List<String> errors = new ArrayList<>();

        if (post == null) {
            errors.add("L'article ne peut pas être null");
            return ValidationResult.error(errors);
        }

        if (!ValidationUtils.isValidTitle(post.getTitre())) {
            errors.add(ValidationUtils.ERROR_TITLE_INVALID);
        }

        if (!ValidationUtils.isValidContent(post.getContenu())) {
            if (post.getContenu() != null && post.getContenu().trim().length() < ValidationUtils.MIN_CONTENT_LENGTH) {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_SHORT);
            } else {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_LONG);
            }
        }

        if (post.getAuteur() == null) {
            errors.add("L'auteur de l'article est obligatoire");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.error(errors);
    }

    /**
     * Valide une entité Message
     */
    public static ValidationResult validateMessage(Message message) {
        List<String> errors = new ArrayList<>();

        if (message == null) {
            errors.add("Le message ne peut pas être null");
            return ValidationResult.error(errors);
        }

        if (!ValidationUtils.isValidName(message.getNom())) {
            errors.add(ValidationUtils.ERROR_NAME_INVALID);
        }

        if (!ValidationUtils.isValidEmail(message.getEmail())) {
            errors.add(ValidationUtils.ERROR_EMAIL_INVALID);
        }

        if (!ValidationUtils.isValidSubject(message.getSujet())) {
            errors.add(ValidationUtils.ERROR_SUBJECT_INVALID);
        }

        if (!ValidationUtils.isValidContent(message.getContenu())) {
            if (message.getContenu() != null && message.getContenu().trim().length() < ValidationUtils.MIN_CONTENT_LENGTH) {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_SHORT);
            } else {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_LONG);
            }
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.error(errors);
    }

    /**
     * Valide une entité Commentaire
     */
    public static ValidationResult validateCommentaire(Commentaire commentaire) {
        List<String> errors = new ArrayList<>();

        if (commentaire == null) {
            errors.add("Le commentaire ne peut pas être null");
            return ValidationResult.error(errors);
        }

        if (!ValidationUtils.isValidContent(commentaire.getContenu())) {
            if (commentaire.getContenu() != null && commentaire.getContenu().trim().length() < ValidationUtils.MIN_CONTENT_LENGTH) {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_SHORT);
            } else {
                errors.add(ValidationUtils.ERROR_CONTENT_TOO_LONG);
            }
        }

        if (commentaire.getPost() == null) {
            errors.add("L'article associé au commentaire est obligatoire");
        }

        if (commentaire.getAuteur() == null) {
            errors.add("L'auteur du commentaire est obligatoire");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.error(errors);
    }

    /**
     * Valide une entité Utilisateur
     */
    public static ValidationResult validateUtilisateur(Utilisateur utilisateur) {
        List<String> errors = new ArrayList<>();

        if (utilisateur == null) {
            errors.add("L'utilisateur ne peut pas être null");
            return ValidationResult.error(errors);
        }

        if (!ValidationUtils.isValidName(utilisateur.getNom())) {
            errors.add(ValidationUtils.ERROR_NAME_INVALID);
        }

        if (!ValidationUtils.isValidEmail(utilisateur.getEmail())) {
            errors.add(ValidationUtils.ERROR_EMAIL_INVALID);
        }

        if (utilisateur.getRole() == null) {
            errors.add("Le rôle de l'utilisateur est obligatoire");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.error(errors);
    }

    /**
     * Classe pour représenter le résultat d'une validation d'entité
     */
    public static class ValidationResult {

        private final boolean valid;
        private final List<String> errors;

        private ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, new ArrayList<>());
        }

        public static ValidationResult error(String error) {
            List<String> errors = new ArrayList<>();
            errors.add(error);
            return new ValidationResult(false, errors);
        }

        public static ValidationResult error(List<String> errors) {
            return new ValidationResult(false, errors);
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }

        public String getAllErrorsAsString() {
            return String.join("; ", errors);
        }
    }
}
