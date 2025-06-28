package dev.gutemberg.post.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.gutemberg.post.ui.events.ShowDetailsEvent;
import dev.gutemberg.post.ui.models.PostSummaryOutput;

public class PostComponent extends VerticalLayout {
    private final Button actionButton = new Button("Read more", VaadinIcon.ANGLE_RIGHT.create(), this::onClick);
    private final transient PostSummaryOutput postSummary;

    public PostComponent(final PostSummaryOutput postSummary) {
        this.postSummary = postSummary;
        create();
        addStyle();
    }

    public void addShowDetailsEventListener(final ComponentEventListener<ShowDetailsEvent> listener) {
        addListener(ShowDetailsEvent.class, listener);
    }

    private void create() {
        add(
                new H1(postSummary.title()),
                new H2(postSummary.author()),
                new Paragraph(postSummary.summary()),
                actionButton
        );
    }

    private void addStyle() {
        addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_40,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.MaxWidth.SCREEN_SMALL
        );
        actionButton.setIconAfterText(true);
        actionButton.getStyle().setCursor("pointer");
    }

    private void onClick(final ClickEvent<Button> event) {
        fireEvent(new ShowDetailsEvent(this, postSummary.id()));
    }
}
