package slice.module.modules.movement;

import slice.Slice;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.util.KeyUtil;
import slice.util.MoveUtil;

@ModuleInfo(name = "Sprint", description = "Sprints for you", category = Category.MOVEMENT)
public class Sprint extends Module {
    
    BooleanValue omni = new BooleanValue("Omni",false);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!omni.getValue()) {
            if(mc.thePlayer.motionX != 0 && mc.thePlayer.motionZ != 0 && mc.thePlayer.motionY != 0 && !mc.thePlayer.isSneaking()
                    && KeyUtil.moveKeys()[0].pressed && (Slice.INSTANCE.getModuleManager().getModule(NoSlow.class).isEnabled() || !mc.thePlayer.isUsingItem())) {
                mc.thePlayer.setSprinting(true);
            }
            return;
        }
        mc.thePlayer.setSprinting(true);
    }

}
