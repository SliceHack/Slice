package slice.module.modules.movement;

import slice.event.Event;
import slice.event.events.EventSlowDown;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "NoSlow", description = "Removes SlowDowns", category = Category.MOVEMENT)
public class NoSlow extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventSlowDown) {
            EventSlowDown e =  (EventSlowDown) event;
            e.setCancelled(true);
        }
    }
}
