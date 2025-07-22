package dev.gutemberg.post.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import dev.gutemberg.post.domain.services.PostService;
import dev.gutemberg.post.ui.annotations.View;
import dev.gutemberg.post.ui.components.PostComponent;
import dev.gutemberg.post.ui.events.CreatePostEvent;
import dev.gutemberg.post.ui.events.ShowDetailsEvent;
import dev.gutemberg.post.ui.models.PostSummaryOutput;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import java.util.Objects;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.*;

@View
public class PostView extends Scroller {
    private static final int INITIAL_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 5;
    private final VerticalLayout content = new VerticalLayout(Alignment.CENTER);
    private final transient PostService postService;
    private final transient ConversionService conversionService;

    public PostView(final PostService postService, final ConversionService conversionService) {
        this.postService = postService;
        this.conversionService = conversionService;
        create();
    }

    public void addShowDetailsListener(final ComponentEventListener<ShowDetailsEvent> listener) {
        addListener(ShowDetailsEvent.class, listener);
    }

    public void addNewPost(final CreatePostEvent event) {
        final var postSummaryOutput = Objects.requireNonNull(
                conversionService.convert(event.getPost(), PostSummaryOutput.class)
        );
        content.addComponentAsFirst(createPost(postSummaryOutput));
    }

    private void create() {
        content.add(searchPosts());
        setContent(content);
        setScrollDirection(ScrollDirection.VERTICAL);
        setWidthFull();
    }

    private Component[] searchPosts() {
        final Pageable pageable = PageRequest.of(INITIAL_PAGE_NUMBER, PAGE_SIZE, Direction.DESC, "createdAt");
        return postService.search(pageable)
                .map(post -> conversionService.convert(post, PostSummaryOutput.class))
                .get()
                .map(this::createPost)
                .toArray(Component[]::new);
    }

    private PostComponent createPost(final PostSummaryOutput summary) {
        final var post = new PostComponent(summary);
        post.addShowDetailsEventListener(this::propagateShowDetailsEvent);
        return post;
    }

    private void propagateShowDetailsEvent(final ShowDetailsEvent event) {
        fireEvent(new ShowDetailsEvent(this, event.getPostId()));
    }
}
