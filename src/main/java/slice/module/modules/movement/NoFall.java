package slice.module.modules.movement;

import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "NoFall", description = "Spoofs you onGround", category = Category.MOVEMENT)
public class NoFall extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            e.setOnGround(true);
        }
    }
}
