package slice.module.modules.misc;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.util.PacketUtil;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Disabler", description = "Disables an anticheat", category = Category.MISC)
@SuppressWarnings("all")
public class Disabler extends Module {

     BooleanValue warzone = new BooleanValue("WarzoneMC", false);
     BooleanValue hypixel = new BooleanValue("Hypixel Strafe", false);

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

     private long index;
     private boolean swap;

    public void onEnable() {
        index = 0;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if((!warzone.getValue()) || mc.isSingleplayer()) return;
        if(!(timer.hasTimeReached(300 + (swap ? 50 : 0)))) return;

        packets.forEach(PacketUtil::sendPacketNoEvent);
        packets.clear();
        swap = !swap;
    }

    @PacketEvent
    public void onC00(C00PacketKeepAlive c00, EventPacket e) {
        if(!warzone.getValue()) return;
        if(mc.isSingleplayer() || !(e.getPacket() instanceof C00PacketKeepAlive)) return;

        e.setCancelled(true);
        packets.add(c00);
        timer.reset();
    }

    @PacketEvent
    public void onS08(S08PacketPlayerPosLook s08, EventPacket e) {
        if(!hypixel.getValue()) return;
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(s08.getX(), s08.getY(), s08.getZ(), true));
        mc.thePlayer.setPosition(s08.getX(), s08.getY(), s08.getZ());
    }

}
