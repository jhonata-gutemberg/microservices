package dev.gutemberg.post.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class ShowDetailsEvent extends ComponentEvent<Component> {
    private final UUID postId;

    public ShowDetailsEvent(final Component source, final UUID postId) {
        super(source, false);
        this.postId = postId;
    }
}
