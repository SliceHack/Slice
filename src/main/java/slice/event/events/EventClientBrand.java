package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventClientBrand extends Event {
    private String brand;
}
