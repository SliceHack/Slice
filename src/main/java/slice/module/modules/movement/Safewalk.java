package slice.module.modules.movement;

import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventSafeWalk;
import slice.event.events.EventSlowDown;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Safewalk", description = "Removes walking off blocks", category = Category.MOVEMENT)
public class Safewalk extends Module {

    @EventInfo
    public void onSafeWalk(EventSafeWalk e) {
        e.setSafeWalk(false);
    }

}