package slice.module.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.KeyUtil;

@ModuleInfo(name = "InvMove", description = "Let's you move when you are in an inventory", category = Category.MOVEMENT)
public class InvMove extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        KeyBinding[] moveKeys = KeyUtil.moveKeys();
        if(mc.currentScreen != null) {

            if(mc.currentScreen instanceof GuiChat)
                return;

            for (KeyBinding key : moveKeys) {
                key.pressed = Keyboard.isKeyDown(key.getKeyCode());
            }

        }
    }

}
