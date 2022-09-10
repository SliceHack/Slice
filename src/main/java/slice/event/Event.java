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
        Slice.INSTANCE.getScriptManager().callEvent(getScriptName(), this);
    }

    /**
     * Gets the name of the script event
     * */
    public String getScriptName() {
        String eventName = this.getClass().getSimpleName().substring(5);
        return eventName.substring(0, 1).toLowerCase() + eventName.substring(1);
    }
}
