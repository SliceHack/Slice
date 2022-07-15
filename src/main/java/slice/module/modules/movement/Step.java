package slice.module.modules.movement;

import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Step", description = "Allows you to step full blocks without jumping", category = Category.MOVEMENT)
public class Step extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla");

    NumberValue height = new NumberValue("Height", 1, 1, 10, NumberValue.Type.INTEGER);

    public void onUpdate(EventUpdate event) {
        height.setVisible(mode.getValue().equals("Vanilla"));
    }

    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            switch (mode.getValue()) {
                case "Vanilla":
                    mc.thePlayer.stepHeight = height.getValue().floatValue();
                    break;
            }
        }
    }
}
