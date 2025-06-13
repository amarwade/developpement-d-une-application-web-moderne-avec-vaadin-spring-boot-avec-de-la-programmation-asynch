package app.project_fin_d_etude.layout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {

    public MainLayout() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        getStyle().set("height", "100vh"); // Assure que le layout prend toute la hauteur de la fenêtre

        // Header
        add(new Header());

    }
}
