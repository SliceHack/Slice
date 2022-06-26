package slice.module.modules.combat;

import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "AntiBot", description = "Removes all of the bots from the game.", category = Category.COMBAT)
public class AntiBot extends Module {
    public void onEvent(Event event) {}
}
