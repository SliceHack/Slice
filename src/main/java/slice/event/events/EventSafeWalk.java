package slice.event.events;

import slice.event.Event;

public class EventSafeWalk extends Event {

    public EventSafeWalk(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
