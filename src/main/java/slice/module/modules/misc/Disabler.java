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
import java.util.ConcurrentModificationException;
import java.util.List;

@ModuleInfo(name = "Disabler", description = "Disables an anticheat", category = Category.MISC)
@SuppressWarnings("all")
public class Disabler extends Module {

     ModeValue mode = new ModeValue("Mode", "WarzoneMC", "WarzoneMC");

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

     private long index, index2;
     private boolean swap;

    public void onEnable() {
        index = 0;
        index2 = 0;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(!mode.getValue().equalsIgnoreCase("WarzoneMC"))
                return;

            if(mc.isSingleplayer())
                return;

            if(timer.hasReached(swap ? 150 : 0)) {
                if(packets.isEmpty())
                    return;

                try {
                    packets.forEach(mc.thePlayer.sendQueue::addToSendNoEvent);
                } catch (ConcurrentModificationException ignored){}

                timer.reset();
                if(index > 2) {
                    swap = !swap;
                    index = 0;
                }
            }
        }

        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();
            switch (mode.getValue()) {
                case "WarzoneMC":
                    if(mc.isSingleplayer())
                        return;

                    if(p instanceof C00PacketKeepAlive) {
                        if(index++ % 2 == 0) {
                            packets.add((C00PacketKeepAlive) p);
                            e.setCancelled(true);
                            return;
                        }
                    }
                    break;
            }
        }
    }
}
