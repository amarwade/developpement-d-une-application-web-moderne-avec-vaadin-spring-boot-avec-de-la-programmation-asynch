package app.project_fin_d_etude.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BlogPostCard extends Div {

    public BlogPostCard(String title, String date, String description) {
        addClassNames(
                LumoUtility.Background.CONTRAST_80,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.MEDIUM
        );
        getStyle().set("border", "1px solid var(--lumo-contrast-10%)");

        H3 cardTitle = new H3(title);
        cardTitle.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        Paragraph cardDate = new Paragraph(date);
        cardDate.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.Margin.Top.SMALL,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassNames(
                LumoUtility.FontSize.MEDIUM
        );

        Button readMoreButton = new Button("Lire plus");
        readMoreButton.addClassNames(
                LumoUtility.Margin.Top.MEDIUM,
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.Padding.Vertical.XSMALL,
                LumoUtility.BorderRadius.SMALL
        );

        VerticalLayout cardContent = new VerticalLayout(cardTitle, cardDate, cardDescription, readMoreButton);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        cardContent.setAlignItems(FlexComponent.Alignment.START);

        add(cardContent);
    }
}
