package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.IChatComponent;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventChatMessage extends Event {
    private IChatComponent chatComponent;
    private final String formattedMessage, unformattedMessage;
}
