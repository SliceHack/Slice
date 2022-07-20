package slice.module.modules.misc;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "FPSBoost", description = "Boosts your fps", category = Category.MISC)
public class FPSBooster extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(timer.hasReached(1000L)) {
            System.gc();
            timer.reset();
        }
    }

}
