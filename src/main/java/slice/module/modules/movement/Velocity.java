package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import slice.event.Event;
import slice.event.events.EventClientTick;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

import java.util.logging.Logger;

@ModuleInfo(name = "Velocity", description = "Removes the velocity of the player", category = Category.MOVEMENT)
public class Velocity extends Module {

    int ticks = 0;

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "Astro", "MMC");

    NumberValue horizontal = new NumberValue("Horizontal", 0.0D, 0.0D, 100.0D, NumberValue.Type.DOUBLE);
    NumberValue vertical = new NumberValue("Vertical", 0.0D, 0.0D, 100.0D, NumberValue.Type.DOUBLE);

    public void onDisable() {
        ticks = 0;
        mc.timer.timerSpeed = 1.0F;
    }

    public void onUpdateNoToggle(EventUpdate event) {
        horizontal.setHidden(!mode.getValue().equalsIgnoreCase("Vanilla"));
        vertical.setHidden(!mode.getValue().equalsIgnoreCase("Vanilla"));
    }

    public void onEvent(Event event) {
        if (event instanceof EventClientTick) {
            switch (mode.getValue()) {
                case "Astro":
                    if (mc.thePlayer.hurtResistantTime > 2) {
                        ticks++;
                    }
                    if (mc.thePlayer.hurtResistantTime >= 16) {
                        ticks = 0;
                    }
                    break;
            }
        }

        if(event instanceof EventPacket) {

            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();

            switch (mode.getValue()) {
                case "Vanilla":
                    if (p instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) p;

                        if (horizontal.getValue().doubleValue() == 0 && vertical.getValue().doubleValue() == 0) {
                            e.setCancelled(true);
                        }

                        s12.motionX *= horizontal.getValue().doubleValue() / 100.0;
                        s12.motionY *= vertical.getValue().doubleValue() / 100.0;
                        s12.motionZ *= horizontal.getValue().doubleValue() / 100.0;
                    }
                    break;
                case "Astro":
                    if (p instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) p;
                        if (ticks > 4) {
                            e.setCancelled(true);
                        }
                    }
                case "MMC":
                    if(p instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) p;
                        s12.motionX *= 50.0D / 100.0;
                        s12.motionZ *= 40.0D / 100.0;
                        s12.motionY *= 50.0D / 100.0;
                    }

                    break;
            }
            if (p instanceof S27PacketExplosion) {
                S27PacketExplosion s27 = (S27PacketExplosion) p;
                e.setCancelled(true);
            }
        }
    }
}
