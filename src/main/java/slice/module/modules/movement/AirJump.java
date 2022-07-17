package slice.module.modules.movement;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.KeyUtil;
import slice.util.MoveUtil;

@ModuleInfo(name = "AirJump", description = "Let's you jump in the air", category = Category.MOVEMENT)
public class AirJump extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!KeyUtil.moveKeys()[4].pressed)
            return;

        MoveUtil.jump();
    }

}
