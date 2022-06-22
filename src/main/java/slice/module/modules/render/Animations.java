package slice.module.modules.render;

import lombok.Getter;
import slice.event.Event;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;

@ModuleInfo(name = "Animations", description = "Block Hit Animation", category = Category.RENDER)
@Getter
public class Animations extends Module {

    ModeValue mode = new ModeValue("Mode", "1.7", "1.7", "Spin");

    public void onEvent(Event event) {}
}
