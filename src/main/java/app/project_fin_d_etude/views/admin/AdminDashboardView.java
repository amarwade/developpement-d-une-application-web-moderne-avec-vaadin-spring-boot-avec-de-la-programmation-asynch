package app.project_fin_d_etude.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import app.project_fin_d_etude.layout.AdminLayout;

@Route(value = "admin/dashboard", layout = AdminLayout.class)
@PageTitle("Dashboard Admin")
public class AdminDashboardView extends VerticalLayout {

    public AdminDashboardView() {
        setSpacing(true);
        setPadding(true);
        setWidthFull();
        getStyle().set("background-color", "var(--lumo-contrast-5pct)");

        // Navigation principale
        HorizontalLayout mainNav = new HorizontalLayout();
        mainNav.setSpacing(true);
        mainNav.setWidthFull();
        mainNav.setJustifyContentMode(JustifyContentMode.START);
        mainNav.getStyle()
                .set("background-color", "white")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("padding", "1rem")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        RouterLink usersLink = createNavLink("Utilisateurs", AdminUsersView.class, VaadinIcon.USERS);
        RouterLink postsLink = createNavLink("Posts", AdminPostsView.class, VaadinIcon.NEWSPAPER);
        RouterLink messagesLink = createNavLink("Messages", AdminMessagesView.class, VaadinIcon.MAILBOX);
        RouterLink commentairesLink = createNavLink("Commentaires", AdminCommentairesView.class, VaadinIcon.COMMENT);

        mainNav.add(usersLink, postsLink, messagesLink, commentairesLink);
        add(mainNav);

        // Section d'activité récente
        VerticalLayout recentActivity = new VerticalLayout();
        recentActivity.setPadding(true);
        recentActivity.getStyle()
                .set("background-color", "white")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
                .set("margin-top", "2rem");

        H2 activityTitle = new H2("Activité Récente");
        activityTitle.getStyle()
                .set("margin-top", "0")
                .set("color", "black")
                .set("font-size", "1.5rem");
        recentActivity.add(activityTitle);

        Span noActivity = new Span("Aucune activité récente");
        noActivity.getStyle()
                .set("color", "black")
                .set("font-style", "italic");
        recentActivity.add(noActivity);

        add(recentActivity);
    }

    private RouterLink createNavLink(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {
        RouterLink link = new RouterLink(navigationTarget);
        link.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem")
                .set("padding", "0.5rem 1rem")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("text-decoration", "none")
                .set("color", "black");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);

        Button button = new Button(icon.create());
        button.getStyle()
                .set("background-color", "var(--lumo-primary-color)")
                .set("color", "white");

        layout.add(button, new Span(text));
        link.add(layout);
        return link;
    }
}
