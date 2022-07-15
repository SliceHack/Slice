package slice.module.modules.render;

import lombok.Setter;
import slice.event.Event;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;

@ModuleInfo(name = "HUD", description = "Renders the client's heads-up-display.", category = Category.RENDER)
@Setter
public class HUD extends Module {

    ModeValue mode = new ModeValue("Mode", "Standard", "Standard", "Smooth");
    BooleanValue playerOnScreen = new BooleanValue("Player On Screen", true);
    BooleanValue targetHUD = new BooleanValue("Target HUD", true);
    BooleanValue bps = new BooleanValue("BPS", true);

    public void init() {
        setEnabled(true);
    }
}
