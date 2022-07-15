package slice.event;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;

/**
 * The Event class
 * the base of every event
 *
 * @author Nick
 * */
@Getter @Setter
public class Event {

    /* cancelled variable **/
    public boolean cancelled;

    /**
     * Calls the event
     * */
    public void call() {
        Slice.INSTANCE.getEventManager().runEvent(this);
    }
}
