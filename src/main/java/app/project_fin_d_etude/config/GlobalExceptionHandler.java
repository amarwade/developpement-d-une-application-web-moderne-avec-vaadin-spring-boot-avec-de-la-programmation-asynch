package app.project_fin_d_etude.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire d'exceptions global pour l'application. Intercepte toutes les
 * exceptions non gérées et retourne des réponses HTTP appropriées.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions de validation (IllegalArgumentException)
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        logger.warn("Erreur de validation: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erreur de validation");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Gère les exceptions de ressource non trouvée
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(
            jakarta.persistence.EntityNotFoundException ex, WebRequest request) {

        logger.warn("Ressource non trouvée: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Ressource non trouvée");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Gère les exceptions d'accès refusé
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {

        logger.warn("Accès refusé: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Accès refusé");
        response.put("message", "Vous n'avez pas les permissions nécessaires pour cette action");
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Gère les exceptions d'authentification
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex, WebRequest request) {

        logger.warn("Erreur d'authentification: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erreur d'authentification");
        response.put("message", "Veuillez vous authentifier pour accéder à cette ressource");
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Gère les exceptions de base de données
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<Map<String, Object>> handleSQLException(
            java.sql.SQLException ex, WebRequest request) {

        logger.error("Erreur de base de données: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erreur de base de données");
        response.put("message", "Une erreur s'est produite lors de l'accès aux données");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gère les exceptions de violation d'intégrité des données
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(
            org.springframework.dao.DataIntegrityViolationException ex, WebRequest request) {

        logger.warn("Violation d'intégrité des données: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Violation d'intégrité des données");
        response.put("message", "Cette ressource existe déjà ou ne peut pas être supprimée");
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Gère toutes les autres exceptions non prévues
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {

        logger.error("Erreur inattendue: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erreur interne du serveur");
        response.put("message", "Une erreur inattendue s'est produite");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
