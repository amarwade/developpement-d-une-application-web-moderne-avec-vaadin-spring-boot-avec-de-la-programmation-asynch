package app.project_fin_d_etude.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.RouterLayout;

public class AdminLayout extends AppLayout implements RouterLayout {

    public AdminLayout() {
        setPrimarySection(Section.DRAWER);
        // Ajoute ici un header/menu admin si besoin
    }
}
