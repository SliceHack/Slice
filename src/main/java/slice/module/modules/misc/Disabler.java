package slice.module.modules.misc;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.util.LoggerUtil;
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
        e.setCancelled(true);
        if(!warzone.getValue()) return;
        if(mc.isSingleplayer() || !(e.getPacket() instanceof C00PacketKeepAlive)) return;

        e.setCancelled(true);
        packets.add(c00);
        timer.reset();
    }

    @PacketEvent
    public void onS08(S08PacketPlayerPosLook s08, EventPacket e) {
        PacketUtil.sendPacket(new C03PacketPlayer());
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(s08.getX(), s08.getY(), s08.getZ(), mc.thePlayer.onGround));
        PacketUtil.sendPacket(new C03PacketPlayer());

        if(!hypixel.getValue()) return;
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(s08.getX(), s08.getY(), s08.getZ(), true));
        mc.thePlayer.setPosition(s08.getX(), s08.getY(), s08.getZ());
    }

    @PacketEvent
    public void onS18(S18PacketEntityTeleport s18, EventPacket e) {
        if(s18.getEntityId() != mc.thePlayer.getEntityId()) return;

        e.setCancelled(true);
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(s18.getX(), s18.getY(), s18.getZ(), mc.thePlayer.onGround));
    }

    @PacketEvent
    public void onC0F(C0FPacketConfirmTransaction c0f, EventPacket e) {
        LoggerUtil.addMessageNoPrefix("");
        LoggerUtil.addMessage("C0F | " + c0f.uid + " | " + c0f.accepted + " | " + c0f.windowId);
        PacketUtil.sendPacketNoEvent(new C0FPacketConfirmTransaction(0, (short) 0, false));
    }

}
