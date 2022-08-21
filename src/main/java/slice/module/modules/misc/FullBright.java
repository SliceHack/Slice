package slice.module.modules.misc;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "FullBright", description = "Makes the world brighter", category = Category.MISC)
public class FullBright extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        mc.gameSettings.gammaSetting = 1000F;
    }

}
