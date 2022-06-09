package slice.module.modules.world;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "TimeChanger", description = "Changes the time of the day.", category = Category.WORLD)
public class TimeChanger extends Module {

    NumberValue time = new NumberValue("Time", 16000L, 0L, 24000L, NumberValue.Type.LONG);

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            mc.theWorld.setWorldTime(time.getValue().longValue());
        }

        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();
            if (p instanceof S03PacketTimeUpdate) {
                e.setCancelled(true);
            }
        }
    }
}
