package slice.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "AntiCrash", description = "Prevents the client from crashing", category = Category.MISC)
public class AntiCrash extends Module {

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();
        if(p instanceof S2APacketParticles) {
            e.setCancelled(true);
        }
    }

}
