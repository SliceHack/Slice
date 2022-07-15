package slice.module.modules.world;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Time", description = "Changes the time of the day.", category = Category.WORLD)
public class TimeChanger extends Module {

    NumberValue time = new NumberValue("Time", 16000L, 0L, 24000L, NumberValue.Type.LONG);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        mc.theWorld.setWorldTime(time.getValue().longValue());
    }

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if (p instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }

}
