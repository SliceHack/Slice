package slice.module.modules.misc;

import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "FullBright", description = "Makes the world brighter", category = Category.MISC)
public class FullBright extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            mc.gameSettings.saturation = 1000F;
        }
    }
}
