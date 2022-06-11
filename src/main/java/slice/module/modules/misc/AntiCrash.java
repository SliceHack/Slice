package slice.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "AntiCrash", description = "Prevents the client from crashing", category = Category.MISC)
public class AntiCrash extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();
            if(p instanceof S2APacketParticles) {
                e.setCancelled(true);
            }
        }
    }
}
