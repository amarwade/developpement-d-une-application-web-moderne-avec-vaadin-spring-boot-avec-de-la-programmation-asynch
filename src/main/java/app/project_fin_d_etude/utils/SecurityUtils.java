package app.project_fin_d_etude.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;

import java.util.stream.Stream;

public class SecurityUtils {

    /**
     * Retourne l’email de l’utilisateur actuellement connecté.
     */
    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName(); // retourne l'email si c’est le principal
        }
        return null;
    }

    /**
     * Vérifie si un utilisateur est connecté.
     */
    public static boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null &&
                Stream.of(HandlerHelper.RequestType.values())
                        .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }
}
