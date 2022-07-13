package slice.module.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.KeyUtil;

@ModuleInfo(name = "AirJump", description = "Let's you jump in the air", category = Category.MOVEMENT)
public class AirJump extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(KeyUtil.moveKeys()[4].pressed) {
                mc.thePlayer.onGround = true;
            }
        }
    }
}
