package slice.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;
import net.optifine.Log;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.KeyUtil;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;
import slice.util.RotationUtil;

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
@SuppressWarnings("all")
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "Dev", "Hycraft", "UwUGuard", "UwUGuardGlide");
    BooleanValue bobbing = new BooleanValue("Bobbing", true);
    NumberValue speed = new NumberValue("Speed", 3.0D, 0.1D, 6.0D, NumberValue.Type.DOUBLE);

    boolean up = false;

    private int stage;

    private int posY;

    public void onEnable() {
        if(mode.getValue().equalsIgnoreCase("UwUGuard") && mc.thePlayer.onGround) {
            MoveUtil.jump();
        }
        if(mode.getValue().equalsIgnoreCase("PixelEdge")) {
            for(double i = 0; i < 3.52F; i += 0.1F) {
                mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ, false));
            }
            mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer(true));
        }
    }

    public void onDisable() {
        stage = 0;
        mc.timer.timerSpeed = 1.0F;
        posY = (int) mc.thePlayer.posY;
    }

    public void onUpdate(EventUpdate event) {
        speed.setHidden(!mode.getValue().equalsIgnoreCase("Vanilla"));
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {

            EventUpdate e = (EventUpdate) event;

            // boobing
            if(bobbing.getValue() && MoveUtil.isMoving()) {
                mc.thePlayer.cameraPitch = 0.1F;
                mc.thePlayer.cameraYaw = 0.1F;
            }

            switch (mode.getValue()) {
                case "Vanilla":
                    if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY = speed.getValue().doubleValue();
                    } else if(mc.gameSettings.keyBindSprint.isKeyDown()) {
                        mc.thePlayer.motionY = -speed.getValue().doubleValue();
                    } else {
                        mc.thePlayer.motionY = up ? 0 : 0.001;
                        up = !up;
                    }
                    MoveUtil.strafe(speed.getValue().doubleValue());
                    break;
                // wip
                case "UwUGuard":
                    if(mc.thePlayer.ticksExisted % 2 == 0) {
                        float yaw = mc.thePlayer.rotationYaw;

                        double x = mc.thePlayer.posX + Math.cos(Math.toRadians(yaw + 90));
                        double z = mc.thePlayer.posZ + Math.sin(Math.toRadians(yaw + 90));
                        double y = mc.thePlayer.posY;
                        mc.thePlayer.setPosition(x, y, z);
                    }
                    break;
                case "UwUGuardGlide":
                    if(mc.thePlayer.onGround) return;
                    MoveUtil.strafe(7);
                    mc.timer.timerSpeed = 0.1f;
                    break;
                case "Hycraft":
                    mc.thePlayer.motionY = 0F;
                    MoveUtil.strafe(0.5D);
                    break;
            }

        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            Packet<?> p = e.getPacket();

            if(mc.theWorld == null)
                return;

            if(mode.getValue().equalsIgnoreCase("Hycraft")) {
                if (p instanceof C03PacketPlayer.C06PacketPlayerPosLook
                        || p instanceof C03PacketPlayer.C04PacketPlayerPosition
                        || p instanceof C03PacketPlayer.C05PacketPlayerLook) {

                    event.setCancelled(mc.thePlayer.ticksExisted % 5 != 0);
                }
            }
            if(mode.getValue().equalsIgnoreCase("Dev")) {
                if(p instanceof S12PacketEntityVelocity) {
                }
            }
        }
    }
}
