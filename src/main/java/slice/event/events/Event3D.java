package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @AllArgsConstructor
public class Event3D extends Event {
    public final float partialTicks;
}
