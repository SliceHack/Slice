package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * For Helping with Keys
 *
 * @author Nick
 * */
@UtilityClass
public class KeyUtil {

    /**
     * returns all move keys
     * */
    public KeyBinding[] moveKeys() {
        return new KeyBinding[] {
                Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight,
                Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindJump,
                Minecraft.getMinecraft().gameSettings.keyBindSneak,
        };
    }

    /**
     * forward key
     * */
    public KeyBinding forward() {
        return Minecraft.getMinecraft().gameSettings.keyBindLeft;
    }
}
