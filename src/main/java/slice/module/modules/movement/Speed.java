package slice.module.modules.movement;

import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.MoveUtil;

@ModuleInfo(name = "Speed", description = "Allows you to move fast!!", key = Keyboard.KEY_X, category = Category.MOVEMENT)
public class Speed extends Module {

    ModeValue mode = new ModeValue("Mode", "Bhop", "Bhop", "Astro");

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
                switch (mode.getValue()) {
                    case "Bhop":
                        if(mc.thePlayer.onGround) {
                            MoveUtil.jump();
                        }
                        MoveUtil.strafe(0.5D);
                        break;
                    case "Astro":
                        if (mc.thePlayer.onGround) {
                            MoveUtil.jump();
                        }

                        if (mc.thePlayer.fallDistance < 0.1 && !mc.thePlayer.onGround) {
                            mc.timer.timerSpeed = 1000f;
                            return;
                        }

                        mc.timer.timerSpeed = 0.1f;
                        break;
                }
        }
    }
}
