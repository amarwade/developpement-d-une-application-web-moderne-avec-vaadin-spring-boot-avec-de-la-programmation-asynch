package app.project_fin_d_etude.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.function.SerializableConsumer;

public class VaadinUtils {

    private static final int NOTIFICATION_DURATION = 3000;
    private static final String LOADING_TEXT = "Chargement en cours...";

    public static void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, NOTIFICATION_DURATION, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }

    public static void showSuccessNotification(String message) {
        showNotification(message, NotificationVariant.LUMO_SUCCESS);
    }

    public static void showErrorNotification(String message) {
        showNotification(message, NotificationVariant.LUMO_ERROR);
    }

    public static void showWarningNotification(String message) {
        showNotification(message, NotificationVariant.LUMO_WARNING);
    }

    public static void showConfirmationDialog(String title, String message, SerializableConsumer<Boolean> onConfirm) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.add(message);

        Button confirmButton = new Button("Confirmer", e -> {
            dialog.close();
            onConfirm.accept(true);
        });
        Button cancelButton = new Button("Annuler", e -> {
            dialog.close();
            onConfirm.accept(false);
        });

        dialog.getFooter().add(cancelButton, confirmButton);
        dialog.open();
    }

    public static void addResponsiveClass(Component component) {
        component.addClassNames(
                "animate__animated",
                "animate__fadeIn",
                "animate__faster"
        );
    }

    public static Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.addClassNames(
                "animate__animated",
                "animate__pulse",
                "animate__infinite"
        );
        return button;
    }

    public static Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.addClassNames(
                "animate__animated",
                "animate__fadeIn"
        );
        return button;
    }

    public static Div createLoadingOverlay() {
        Div overlay = new Div();
        overlay.addClassNames(
                "loading-overlay",
                "animate__animated",
                "animate__fadeIn"
        );

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.addClassNames("loading-progress");

        Div loadingText = new Div();
        loadingText.setText(LOADING_TEXT);
        loadingText.addClassNames("loading-text");

        overlay.add(progressBar, loadingText);
        return overlay;
    }

    public static void showLoading(Component parent) {
        Div overlay = createLoadingOverlay();
        if (parent instanceof Div) {
            ((Div) parent).add(overlay);
        }
    }

    public static void hideLoading(Component parent) {
        parent.getChildren()
                .filter(component -> component instanceof Div
                && ((Div) component).getClassNames().contains("loading-overlay"))
                .findFirst()
                .ifPresent(component -> ((Div) parent).remove(component));
    }
}
