package slice.module.modules.movement;

import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "NoFall", description = "Spoofs you onGround", category = Category.MOVEMENT)
public class NoFall extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        e.setOnGround(true);
    }

}
