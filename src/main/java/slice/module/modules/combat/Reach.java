package slice.module.modules.combat;

import lombok.Getter;
import lombok.Setter;
import slice.event.Event;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Reach", description = "Gives you long arms", category = Category.COMBAT)
@Getter @Setter
public class Reach extends Module {

    NumberValue blockReach = new NumberValue("Block Reach", 4.5F, 4.5F, 6.0D, NumberValue.Type.DOUBLE);
    NumberValue reach = new NumberValue("Hit Reach", 3.0D, 3.0D, 6.0D, NumberValue.Type.DOUBLE);


    public void onEvent(Event event) {}
}
