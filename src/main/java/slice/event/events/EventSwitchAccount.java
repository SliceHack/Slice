package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Session;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventSwitchAccount extends Event {
    private Session session, lastSession;
    private String username, uuid;
}
