package dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcomponent {

    @Documented
    @Target({ElementType.TYPE})
    public @interface Builder {
    }

    Class<?>[] modules() default {};
}
