package slice.module.modules.combat;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "AutoPot", description = "Automatically splash potions", category = Category.COMBAT)
public class AutoPot extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {

    }


}
