package app.project_fin_d_etude.layout;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Footer extends HorizontalLayout {

    public Footer() {
        setWidthFull();
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassNames(LumoUtility.Background.CONTRAST_80, LumoUtility.TextColor.PRIMARY, LumoUtility.Padding.Vertical.MEDIUM);

        HorizontalLayout footerLinks = new HorizontalLayout();
        footerLinks.addClassNames(LumoUtility.Gap.SMALL);
        footerLinks.add(
                new Paragraph("© 2023"),
                new Anchor("https://twitter.com/", "Twitter"),
                new Anchor("https://www.linkedin.com/in/amar-wade/", "LinkedIn"),
                new Anchor("amarwade927@gmail.com", "Email"),
                new Anchor("https://github.com/amarwade", "Github"),
                new Anchor("https://maps.app.goo.gl/nn9wd74L5pT52QK68", "Adresse")
        );

        add(footerLinks);
    }
}
