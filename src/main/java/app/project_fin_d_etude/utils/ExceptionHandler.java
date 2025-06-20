package app.project_fin_d_etude.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Classe utilitaire pour centraliser la gestion des exceptions. Fournit des
 * méthodes pour gérer les erreurs de manière cohérente.
 */
public final class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    // Messages d'erreur standardisés
    public static final String ERROR_GENERIC = "Une erreur inattendue s'est produite";
    public static final String ERROR_DATABASE = "Erreur de base de données";
    public static final String ERROR_NETWORK = "Erreur de connexion réseau";
    public static final String ERROR_VALIDATION = "Erreur de validation des données";
    public static final String ERROR_AUTHENTICATION = "Erreur d'authentification";
    public static final String ERROR_AUTHORIZATION = "Erreur d'autorisation";
    public static final String ERROR_NOT_FOUND = "Ressource non trouvée";
    public static final String ERROR_DUPLICATE = "Cette ressource existe déjà";

    private ExceptionHandler() {
        // Classe utilitaire, constructeur privé
    }

    /**
     * Gère une exception de manière générique
     */
    public static void handleException(Exception e, String context) {
        logger.error("Erreur dans {}: {}", context, e.getMessage(), e);
    }

    /**
     * Gère une exception avec un callback personnalisé
     */
    public static void handleException(Exception e, String context, Consumer<String> errorCallback) {
        handleException(e, context);
        String userMessage = getUserFriendlyMessage(e);
        errorCallback.accept(userMessage);
    }

    /**
     * Exécute une opération avec gestion d'erreur
     */
    public static <T> T executeWithErrorHandling(ThrowingSupplier<T> operation, String context, Consumer<String> errorCallback) {
        try {
            return operation.get();
        } catch (Exception e) {
            handleException(e, context, errorCallback);
            return null;
        }
    }

    /**
     * Exécute une opération void avec gestion d'erreur
     */
    public static void executeWithErrorHandling(ThrowingRunnable operation, String context, Consumer<String> errorCallback) {
        try {
            operation.run();
        } catch (Exception e) {
            handleException(e, context, errorCallback);
        }
    }

    /**
     * Retourne un message utilisateur convivial basé sur le type d'exception
     */
    public static String getUserFriendlyMessage(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return ERROR_VALIDATION;
        } else if (e instanceof java.sql.SQLException) {
            return ERROR_DATABASE;
        } else if (e instanceof java.net.ConnectException) {
            return ERROR_NETWORK;
        } else if (e instanceof org.springframework.security.access.AccessDeniedException) {
            return ERROR_AUTHORIZATION;
        } else if (e instanceof org.springframework.security.core.AuthenticationException) {
            return ERROR_AUTHENTICATION;
        } else if (e instanceof jakarta.persistence.EntityNotFoundException) {
            return ERROR_NOT_FOUND;
        } else if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
            return ERROR_DUPLICATE;
        } else {
            return ERROR_GENERIC;
        }
    }

    /**
     * Interface fonctionnelle pour les opérations qui peuvent lever des
     * exceptions
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {

        T get() throws Exception;
    }

    /**
     * Interface fonctionnelle pour les opérations void qui peuvent lever des
     * exceptions
     */
    @FunctionalInterface
    public interface ThrowingRunnable {

        void run() throws Exception;
    }

    /**
     * Gère une exception spécifique à la validation
     */
    public static void handleValidationError(String field, String message, Consumer<String> errorCallback) {
        String errorMessage = String.format("Erreur de validation pour '%s': %s", field, message);
        logger.warn(errorMessage);
        errorCallback.accept(message);
    }

    /**
     * Gère une exception spécifique à la base de données
     */
    public static void handleDatabaseError(Exception e, Consumer<String> errorCallback) {
        logger.error("Erreur de base de données: {}", e.getMessage(), e);
        errorCallback.accept(ERROR_DATABASE);
    }

    /**
     * Gère une exception spécifique au réseau
     */
    public static void handleNetworkError(Exception e, Consumer<String> errorCallback) {
        logger.error("Erreur de réseau: {}", e.getMessage(), e);
        errorCallback.accept(ERROR_NETWORK);
    }

    /**
     * Gère une exception spécifique à l'authentification
     */
    public static void handleAuthenticationError(Exception e, Consumer<String> errorCallback) {
        logger.error("Erreur d'authentification: {}", e.getMessage(), e);
        errorCallback.accept(ERROR_AUTHENTICATION);
    }

    /**
     * Gère une exception spécifique à l'autorisation
     */
    public static void handleAuthorizationError(Exception e, Consumer<String> errorCallback) {
        logger.error("Erreur d'autorisation: {}", e.getMessage(), e);
        errorCallback.accept(ERROR_AUTHORIZATION);
    }
}
