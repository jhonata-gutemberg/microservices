package dev.gutemberg.post.ui.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Route
public class MainView extends HorizontalLayout {
    private final FormView formView;
    private final PostView postView;
    private final DetailsView detailsView;

    public MainView(final FormView formView, final PostView postView, final DetailsView detailsView) {
        this.formView = formView;
        this.postView = postView;
        this.detailsView = detailsView;
        create();
        addStyle();
    }

    private void create() {
        formView.addCreatePostEventListener(postView::addNewPost);
        postView.addShowDetailsListener(detailsView::showDetails);
        add(formView, postView, detailsView);
    }

    private void addStyle() {
        setHeightFull();
        setSpacing(false);
        postView.addClassNames(Border.LEFT, BorderColor.CONTRAST_20);
        detailsView.addClassNames(Border.LEFT, BorderColor.CONTRAST_20);
    }
}
