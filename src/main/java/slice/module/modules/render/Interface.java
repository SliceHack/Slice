package slice.module.modules.render;

import lombok.Getter;
import lombok.Setter;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;

@ModuleInfo(name = "Interface", description = "The Interface of Slice", category = Category.RENDER)
@Getter @Setter
public class Interface extends Module {

    BooleanValue clearChat = new BooleanValue("Transparent Chat", true);
    BooleanValue fontChat = new BooleanValue("Font Chat", true);

    ModeValue fontChatMode = new ModeValue("Font Mode", "Poppins", "Poppins", "Arial");

    public void onUpdate(EventUpdate event) {
        fontChatMode.setHidden(!fontChat.getValue());
    }

    public void onEnable() {
        setEnabled(false);
    }

    public void onEvent(Event event) {}
}
