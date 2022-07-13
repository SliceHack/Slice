package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventClientTick;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.KeyUtil;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;

@ModuleInfo(name = "Speed", description = "Allows you to move fast!!", key = Keyboard.KEY_X, category = Category.MOVEMENT)
public class Speed extends Module {

    ModeValue mode = new ModeValue("Mode", "Bhop", "Bhop", "Hycraft", "Dev", "Astro", "MMC", "UwUGuard", "Legit");

    int onGroundTicks, offGroundTicks;

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        onGroundTicks = 0;
        offGroundTicks = 0;
        KeyUtil.moveKeys()[0].pressed = false;
        KeyUtil.moveKeys()[2].pressed = false;
        KeyUtil.moveKeys()[4].pressed = false;
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
                       if(mc.thePlayer.fallDistance > 4)
                           return;

                       if(!MoveUtil.isMoving()) return;

                       if(mc.thePlayer.onGround) {
                           MoveUtil.jump();
                           MoveUtil.strafe(0.48);
                       }

                       if(offGroundTicks >= 7) {
                           mc.thePlayer.motionY = -2F;
                       }
                       break;
                    case "UwUGuard":
                        if (!MoveUtil.isMoving()) return;

                        if(mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0F;
                            MoveUtil.strafe(0.5D);
                        }
                        break;
                    case "Legit":
                        mc.thePlayer.setSprinting(true);
                        KeyUtil.moveKeys()[0].pressed = true;
                        KeyUtil.moveKeys()[4].pressed = true;

                        int setYaw = 0;
                        int direction = MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
                        switch(direction) {
                            case 0:
                                setYaw = 0;
                                break;
                            case 1:
                                setYaw = 90;
                                break;
                            case 2:
                                setYaw = 180;
                                break;
                            case 3:
                                setYaw = -90;
                                break;
                        }

                        if(mc.thePlayer.onGround) {
                            mc.thePlayer.rotationYaw = setYaw;
                            KeyUtil.moveKeys()[2].pressed = false;
                            break;
                        }

                        if (mc.thePlayer.lastReportedYaw == setYaw) {
                            mc.thePlayer.rotationYaw = setYaw + 44.99F;
                            KeyUtil.moveKeys()[2].pressed = true;
                        }

                        break;
                    case "Dev":
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
