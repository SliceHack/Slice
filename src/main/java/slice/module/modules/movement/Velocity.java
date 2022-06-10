package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Velocity", description = "Removes the velocity of the player", category = Category.MOVEMENT)
public class Velocity extends Module {

    NumberValue horizontal = new NumberValue("Horizontal", 0.0D, 0.0D, 100.0D, NumberValue.Type.DOUBLE);
    NumberValue vertical = new NumberValue("Vertical", 0.0D, 0.0D, 100.0D, NumberValue.Type.DOUBLE);

    public void onEvent(Event event) {
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();

            if(p instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) p;

                if(horizontal.getValue().doubleValue() == 0 && vertical.getValue().doubleValue() == 0) {
                    e.setCancelled(true);
                }

                s12.motionX *= horizontal.getValue().doubleValue() / 100.0;
                s12.motionY *= vertical.getValue().doubleValue() / 100.0;
                s12.motionZ *= horizontal.getValue().doubleValue() / 100.0;
            }
            if(p instanceof S27PacketExplosion) {
                S27PacketExplosion s27 = (S27PacketExplosion) p;
                e.setCancelled(true);
            }
        }
    }
}
