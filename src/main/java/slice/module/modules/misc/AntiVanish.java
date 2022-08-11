package slice.module.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import slice.event.data.EventInfo;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.notification.Notification;
import slice.notification.NotificationManager;
import slice.notification.Type;

@ModuleInfo(name = "AntiVanish", description = "Finds vanished players", category = Category.MISC)
public class AntiVanish extends Module {

    @EventInfo
    public void onPacket(EventPacket e) {
        if(mc.theWorld == null) return;
        if(mc.thePlayer == null) return;
        if(mc.isSingleplayer()) return;

        Packet<?> packet = e.getPacket();

        if(packet instanceof S1DPacketEntityEffect) {
            S1DPacketEntityEffect s1d = (S1DPacketEntityEffect) packet;

            if(mc.theWorld.getEntityByID(s1d.getEntityId()) != null) return;

            foundVanish();
        }

        if(packet instanceof S14PacketEntity) {
            S14PacketEntity s14 = (S14PacketEntity) e.getPacket();

            if(s14.getEntity(mc.theWorld) != null) return;

            foundVanish();
        }
    }

    private void foundVanish() {
        if(timer.hasTimeReached(5000L)) {
            NotificationManager.queue(new Notification(Type.WARNING, "A Player is vanished", 4));
            timer.reset();
        }

    }

}
