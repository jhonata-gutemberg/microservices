package dev.gutemberg.post.ui.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import dev.gutemberg.post.domain.services.PostService;
import dev.gutemberg.post.ui.events.ShowDetailsEvent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Optional;

import static com.vaadin.flow.dom.Style.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DetailsView extends VerticalLayout {
    private final H1 title = new H1();
    private final H2 author = new H2();
    private final Span wordsCount = new Span();
    private final Span cost = new Span();
    private final Paragraph body = new Paragraph();
    private final transient PostService postService;

    public DetailsView(final PostService postService) {
        this.postService = postService;
        create();
        addStyle();
    }

    public void showDetails(final ShowDetailsEvent event) {
        this.postService.getOne(event.getPostId()).ifPresent(post -> {
            title.setText(post.getTitle());
            author.setText(post.getAuthor());
            wordsCount.setText(convertToString(post.getWordCount()));
            cost.setText(convertToString(post.getCalculatedValue()));
            body.setText(post.getBody());
        });
        showView();
    }

    private String convertToString(final Optional<?> optional) {
        return optional.map(String::valueOf).orElse("N/A");
    }

    private void showView() {
        setWidthFull();
        setPadding(true);
        getStyle().setWhiteSpace(WhiteSpace.INITIAL)
                .set("overflow-y", "scroll")
                .setOpacity("100%");
    }

    private void create() {
        add(title);
        add(author);
        add(info());
        add(body);
    }

    private Div info() {
        return new Div(
                new Text("words count: "), wordsCount,
                new Text(" - "),
                new Text("total cost: "), cost
        );
    }

    private void addStyle() {
        hideView();
        wordsCount.getStyle().setFontWeight("bold");
        cost.getStyle().setFontWeight("bold");
    }

    private void hideView() {
        setWidth(0, Unit.PIXELS);
        setPadding(false);
        getStyle().setWhiteSpace(WhiteSpace.NOWRAP)
                .setOverflow(Overflow.HIDDEN)
                .setOpacity("0")
                .setTransition("padding 0.1s, width 0.2s");
    }
}
