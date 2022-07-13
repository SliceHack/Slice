package slice.module.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;
import slice.util.KeyUtil;

@ModuleInfo(name = "Step", description = "Allows you to step full blocks without jumping", category = Category.MOVEMENT)
public class Step extends Module {

    NumberValue height = new NumberValue("Height", 1, 1, 10, NumberValue.Type.INTEGER);

    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {

            mc.thePlayer.stepHeight = height.getValue().floatValue();
        }
    }
}
