package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class Event2D extends Event {
    private float partialTicks;
}
