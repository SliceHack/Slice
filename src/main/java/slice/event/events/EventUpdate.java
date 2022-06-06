package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventUpdate extends Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround, pre;
}
