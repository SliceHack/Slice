package slice.module.modules.movement;

import slice.event.Event;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Fly", description = "Allows you to fly like a bird", category = Category.MOVEMENT)
public class Fly extends Module {

    public void onEvent(Event event) {
    }
}
