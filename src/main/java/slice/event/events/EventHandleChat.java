package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventHandleChat extends Event {
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private S02PacketChat s02PacketChat;
}
