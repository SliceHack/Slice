package slice.module.modules.misc;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;
import slice.util.PacketUtil;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@ModuleInfo(name = "Disabler", description = "Disables an anticheat", category = Category.MISC)
@SuppressWarnings("all")
public class Disabler extends Module {

     ModeValue mode = new ModeValue("Mode", "WarzoneMC", "WarzoneMC", "Hypixel", "Dev");

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

     private long index, index2;
     private boolean swap;

    public void onEnable() {
        index = 0;
        index2 = 0;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        switch (mode.getValue()) {
             case "WarzoneMC":
                  if(mc.isSingleplayer())
                      return;
                  if(timer.hasTimeReached(300 + (swap ? 50 : 0))) {
                      packets.forEach(PacketUtil::sendPacketNoEvent);
                      packets.clear();
                      swap = !swap;
                  } 
                  break;
        }
    }

    @PacketEvent
    public void onPacket(EventPacket e) {
        switch (mode.getValue()) {
             case "WarzoneMC":
                  if(mc.isSingleplayer() || !(e.getPacket() instanceof C00PacketKeepAlive))
                      return;
                  e.setCancelled(true);
                  packets.add(c00);
                  timer.reset();
                  break;
             case "Hypixel":
                  // strafe xd
                  Packet packet = e.getPacket();
                  if(packet instanceof S08PacketPlayerPosLook) {
                      final S08PacketPlayerPosLook wrapper = (S08PacketPlayerPosLook) packet;
                      PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(wrapper.getX(), wrapper.getY(), wrapper.getZ(), true));
                      mc.thePlayer.setPosition(wrapper.getX(), wrapper.getY(), wrapper.getZ());
                  }
                  break;
        }
    }
}
