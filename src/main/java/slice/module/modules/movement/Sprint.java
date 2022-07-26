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

    }

}
