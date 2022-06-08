package slice.module.modules.movement;

import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.MoveUtil;

@ModuleInfo(name = "Speed", description = "Allows you to move fast!!", key = Keyboard.KEY_X, category = Category.MOVEMENT)
public class Speed extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(MoveUtil.isMoving()) {
//                if (mc.thePlayer.onGround) {
//                    MoveUtil.jump();
//                }
                MoveUtil.strafe(0.42f);
            }
        }
    }
}
