package slice.module.modules.movement;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.util.MoveUtil;

@ModuleInfo(name = "Sprint", description = "Sprints for you", category = Category.MOVEMENT)
public class Sprint extends Module {
    
    BooleanValue omni = new BooleanValue("Omni",false);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!MoveUtil.isMoving()) return;

        if(omni.getValue()) {
            mc.thePlayer.setSprinting(true);
            return;
        }

        if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem()) {
            mc.thePlayer.setSprinting(true);
        }
    }

}
