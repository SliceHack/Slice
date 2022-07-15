package slice.module.modules.movement;

import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventSlowDown;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "NoSlow", description = "Removes SlowDowns", category = Category.MOVEMENT)
public class NoSlow extends Module {

    @EventInfo
    public void onSlowDown(EventSlowDown e) {
        e.setCancelled(true);
    }

}
