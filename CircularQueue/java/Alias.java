import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Alias {
    Class<?> value() default Void.class; // Default value to indicate no class alias
    String method() default "";          // Default value to indicate no method alias
}

