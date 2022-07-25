package slice.event.data;

import java.lang.annotation.*;

/**
 * The info of an event method
 *
 * @author Nick
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventInfo {
}
