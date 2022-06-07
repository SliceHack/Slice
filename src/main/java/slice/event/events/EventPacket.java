package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventPacket extends Event {
    private Packet<?> packet;
    private boolean outgoing;

    public boolean isIncomming() {
        return !outgoing;
    }
}
