package dev.gutemberg.post.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.ui.views.FormView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostEvent extends ComponentEvent<FormView> {
    private final transient Post post;

    public CreatePostEvent(final FormView source, final Post post) {
        super(source, false);
        this.post = post;
    }
}
