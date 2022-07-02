package slice.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.*;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Disabler", description = "Disables an anticheat", category = Category.MISC)
@SuppressWarnings("all")
public class Disabler extends Module {

     ModeValue mode = new ModeValue("Mode", "WarzoneMC", "WarzoneMC");

     public final List<C00PacketKeepAlive> packets = new ArrayList<>();

     private long index, index2;

    public void onEnable() {
        index = 0;
        index2 = 0;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(!mode.getValue().equalsIgnoreCase("WarzoneMC"))
                return;

            if(index % 2 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive());
                mc.thePlayer.sendQueue.addToSendNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
            }

            if(timer.hasReached(5000L)) {
                packets.forEach(mc.thePlayer.sendQueue::addToSendNoEvent);
                timer.reset();
            }
        }

        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();
            switch (mode.getValue()) {
                case "WarzoneMC":
                    if(p instanceof C00PacketKeepAlive) {
                        packets.add((C00PacketKeepAlive) p);
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }
}
