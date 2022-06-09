package slice.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Disabler", description = "Disables an anticheat", category = Category.MISC)
@SuppressWarnings("all")
public class Disabler extends Module {

     ModeValue mode = new ModeValue("Mode", "WarzoneMC", "WarzoneMC");

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(!mode.getValue().equalsIgnoreCase("WarzoneMC"))
                return;

            if(timer.hasReached(1000L)) {
                packets.forEach(mc.thePlayer.sendQueue::addToSendNoEvent);
                packets.clear();
            }
        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();

            if(p instanceof C00PacketKeepAlive) {
                e.setCancelled(true);
                packets.add((C00PacketKeepAlive) p);
                timer.reset();
            }
        }
    }
}
