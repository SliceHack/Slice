package slice.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
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
import slice.util.MoveUtil;
import slice.util.RotationUtil;

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "UwUGuard");
    BooleanValue bobbing = new BooleanValue("Bobbing", true);
    NumberValue speed = new NumberValue("Speed", 3.0D, 0.1D, 6.0D, NumberValue.Type.DOUBLE);

    boolean up = false;

    public void onDisable() {
        if(mode.getValue().equalsIgnoreCase("UwUGuard") && mc.thePlayer.onGround) {
            MoveUtil.jump();
        }
        mc.timer.timerSpeed = 1.0F;
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
                case "UwUGuard":
                    double direction = RotationUtil.getDirection();

                    mc.timer.timerSpeed = 0.1f;

                    if(mc.thePlayer.ticksExisted % 4 != 0) {
                        MoveUtil.jump();
                    }
                    if(mc.thePlayer.ticksExisted % 5 != 0) {
                        mc.thePlayer.motionX = -MathHelper.sin((float) direction);
                        mc.thePlayer.motionZ = MathHelper.cos((float) direction);
                        return;
                    }
                    mc.thePlayer.motionY = 0F;
                    mc.thePlayer.motionX = 0F;
                    mc.thePlayer.motionZ = 0F;
                    for (KeyBinding keyBinding : KeyUtil.moveKeys()) {
                        keyBinding.pressed = false;
                    }
                    break;
            }

        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if(mc.theWorld == null)
                return;

        }
    }
}
