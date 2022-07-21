package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @AllArgsConstructor @Setter
public class EventSafeWalk extends Event {
    private boolean safeWalk;
}
