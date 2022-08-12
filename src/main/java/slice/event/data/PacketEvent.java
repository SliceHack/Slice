package slice.event.data;

import java.lang.annotation.*;

/**
 * The info of a packet event method
 *
 * @author Nick
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketEvent {}
