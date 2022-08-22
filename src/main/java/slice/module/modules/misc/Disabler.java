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

     ModeValue mode = new ModeValue("Mode", "WarzoneMC", "WarzoneMC", "Dev");

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

     private long index, index2;
     private boolean swap;

    public void onEnable() {
        index = 0;
        index2 = 0;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!mode.getValue().equalsIgnoreCase("WarzoneMC"))
            return;

        if(mc.isSingleplayer())
            return;

        if(timer.hasTimeReached(300 + (swap ? 50 : 0))) {
            packets.forEach(PacketUtil::sendPacketNoEvent);
            packets.clear();
            swap = !swap;
        }
    }

    @EventInfo
    public void onEvent(EventPacket e) {
        Packet p = e.getPacket();

        if(p instanceof C00PacketKeepAlive) {
            e.setCancelled(true);
            packets.add((C00PacketKeepAlive) p);
            timer.reset();
        }
    }
}
