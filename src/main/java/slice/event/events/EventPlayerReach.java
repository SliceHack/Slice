package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventPlayerReach extends Event {
    private double reach;
}
