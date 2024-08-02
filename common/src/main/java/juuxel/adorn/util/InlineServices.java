package juuxel.adorn.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks {@code @InlineServices.Getter} for inlining in the target class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InlineServices {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface Getter {
    }
}
