package slice.module.modules.movement;

import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventKey;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
public class Fly extends Module {

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = 0.5;
            } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionY = -0.5;
            } else {
                mc.thePlayer.motionY = 0;
            }
        }
    }
}
