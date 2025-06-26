package dev.gutemberg.post.ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import dev.gutemberg.post.domain.entities.Post;
import dev.gutemberg.post.domain.services.PostService;
import dev.gutemberg.post.ui.events.CreatePostEvent;
import dev.gutemberg.post.ui.models.PostInput;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FormView extends VerticalLayout {
    private final TextField title = new TextField("Title", "New post title");
    private final TextField author = new TextField("Author", "John Doe");
    private final TextArea body = new TextArea("Message", "Happy coding...");
    private final Button action = new Button("Post now", VaadinIcon.ROCKET.create(), this::onClick);
    private final Binder<PostInput> binder = new Binder<>();
    private final transient PostService postService;

    public FormView(final PostService postService) {
        this.postService = postService;
        create();
        bindFields();
        addStyle();
    }

    public void addCreatePostEventListener(final ComponentEventListener<CreatePostEvent> listener) {
        addListener(CreatePostEvent.class, listener);
    }

    private void create() {
        add(new H1("Create a new post"), title, author, body, action);
    }

    private void bindFields() {
        binder.forField(title)
                .asRequired("Title is required")
                .bind(PostInput::getTitle, PostInput::setTitle);
        binder.forField(author)
                .asRequired("Author is required")
                .bind(PostInput::getAuthor, PostInput::setAuthor);
        binder.forField(body).asRequired("Message is required")
                .bind(PostInput::getMessage, PostInput::setMessage);
    }

    private void addStyle() {
        title.setWidthFull();
        title.addClassName(MaxWidth.SCREEN_SMALL);
        author.setWidthFull();
        author.addClassName(MaxWidth.SCREEN_SMALL);
        body.setWidthFull();
        body.addClassNames(MaxWidth.SCREEN_SMALL, MaxHeight.FULL);
        body.setMinHeight(150, Unit.PIXELS);
        action.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        action.getStyle().setCursor("pointer");
        action.setIconAfterText(true);
    }

    private void onClick(final ClickEvent<Button> event) {
        final var postInput = new PostInput();
        if (binder.writeBeanIfValid(postInput)) {
            final var post = postService.create(convert(postInput));
            fireEvent(new CreatePostEvent(this, post));
            binder.readBean(new PostInput());
        }
    }

    private Post convert(final PostInput postInput) {
        return new Post(postInput.getTitle(), postInput.getMessage(), postInput.getAuthor());
    }
}
