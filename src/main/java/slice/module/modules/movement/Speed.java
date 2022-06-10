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

    ModeValue mode = new ModeValue("Mode", "Bhop", "Bhop", "Dev", "Astro", "UwUGuard");

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
                switch (mode.getValue()) {
                    case "Bhop":
                        if(MoveUtil.isMoving()) {
                            if (mc.thePlayer.onGround) {
                                MoveUtil.jump();
                            }
                            MoveUtil.strafe((MoveUtil.getSpeed())+0.02);
                        }
                        break;
                    case "Dev":
                        break;
                    case "Astro":
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                        }

                        if (mc.thePlayer.fallDistance < 0.1 && !mc.thePlayer.onGround) {
                            mc.timer.timerSpeed = 1000f;
                            return;
                        }

                        if(!mc.thePlayer.onGround)
                            mc.timer.timerSpeed = 0.1f;

                        break;
                    case "UwUGuard":
                        if (!MoveUtil.isMoving()) return;
                        if (mc.thePlayer.onGround) {
                            MoveUtil.jump();
                        }
                        
                        MoveUtil.strafe(5);
                        break;
                }
        }
    }


}
