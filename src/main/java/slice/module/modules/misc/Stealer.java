package slice.module.modules.misc;

import net.minecraft.inventory.Container;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Stealer", description = "Steals from a chest", category = Category.MISC)
public class Stealer extends Module {

    NumberValue delay = new NumberValue("Delay", 1000, 50, 10000, NumberValue.Type.LONG);

    @EventInfo
    public void onUpdate(EventUpdate e) {

    }
}
