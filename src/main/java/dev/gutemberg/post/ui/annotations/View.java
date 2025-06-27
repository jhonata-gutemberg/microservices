package dev.gutemberg.post.ui.annotations;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public @interface View {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
