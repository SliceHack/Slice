package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventClientTick;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;

@ModuleInfo(name = "Speed", description = "Allows you to move fast!!", key = Keyboard.KEY_X, category = Category.MOVEMENT)
public class Speed extends Module {

    ModeValue mode = new ModeValue("Mode", "Bhop", "Bhop", "Hycraft", "Dev", "Astro", "MMC", "UwUGuard");

    int onGroundTicks, offGroundTicks;

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        onGroundTicks = 0;
        offGroundTicks = 0;
    }

    public void onEvent(Event event) {
        if(event instanceof EventClientTick) {
            if(mc.thePlayer.onGround) {
                offGroundTicks = 0;
                onGroundTicks++;
            } else {
                onGroundTicks = 0;
                offGroundTicks++;
            }
        }
        if(event instanceof EventUpdate) {
                switch (mode.getValue()) {
                    case "Hycraft":
                    case "Bhop":
                        if(!MoveUtil.isMoving()) return;

                        if (mc.thePlayer.onGround) {
                            MoveUtil.jump();
                        }
                        MoveUtil.strafe((MoveUtil.getSpeed())+0.02);
                        break;
                    case "MMC":
                        if(!MoveUtil.isMoving()) return;

                        if(mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.56F;
                            MoveUtil.strafe(0.48D);
                        }
                        break;
                   case "Astro":
                        if(!MoveUtil.isMoving()) return;

                        if(mc.thePlayer.onGround) {
                            MoveUtil.jump();
                            MoveUtil.strafe(0.48);
                        }

                        if(offGroundTicks > 8) {
                          mc.thePlayer.motionY = -1F;
                        }
                        break;
                    case "UwUGuard":
                        if (!MoveUtil.isMoving()) return;

                        if(mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0F;
                            MoveUtil.strafe(0.5D);
                        }
                        break;
                }
        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();

            if(mc.theWorld == null) return;

            if(mode.getValue().equalsIgnoreCase("Hycraft")) {
                if(p instanceof C03PacketPlayer.C06PacketPlayerPosLook
                        || p instanceof C03PacketPlayer.C04PacketPlayerPosition
                        || p instanceof C03PacketPlayer.C05PacketPlayerLook) {

                    event.setCancelled(mc.thePlayer.ticksExisted % 5 != 0);
                }
            }
        }
    }


}
