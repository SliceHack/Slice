package slice.module.modules.combat;

import lombok.Getter;
import lombok.Setter;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventPlayerReach;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Reach", description = "Gives you long arms", category = Category.COMBAT)
@Getter @Setter
public class Reach extends Module {

    NumberValue reach = new NumberValue("Reach", 3.0D, 3.0D, 6.0D, NumberValue.Type.DOUBLE);

    @EventInfo
    public void onPlayerReach(EventPlayerReach e) {
        e.setReach(reach.getValue().doubleValue());
    }
}
