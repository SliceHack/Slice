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
@SuppressWarnings("all")
public class KeyUtil {

    /**
     * returns all move keys
     * */
    public KeyBinding[] moveKeys() {
        return new KeyBinding[] {
                Minecraft.getMinecraft().gameSettings.keyBindForward, Minecraft.getMinecraft().gameSettings.keyBindBack,
                Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight,
                Minecraft.getMinecraft().gameSettings.keyBindJump,
        };
    }

    /**
     * sneak key
     * */
    public KeyBinding sneak() {
        return Minecraft.getMinecraft().gameSettings.keyBindSneak;
    }

    /**
     * forward key
     * */
    public KeyBinding forward() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward;
    }

    /**
     * backward key
     * */
    public KeyBinding back() {
        return Minecraft.getMinecraft().gameSettings.keyBindBack;
    }

    /**
     * left key
     * */
    public KeyBinding left() {
        return Minecraft.getMinecraft().gameSettings.keyBindLeft;
    }

    /**
     * right key
     * */
    public KeyBinding right() {
        return Minecraft.getMinecraft().gameSettings.keyBindRight;
    }

    /**
     * jump key
     * */
    public KeyBinding jump() {
        return Minecraft.getMinecraft().gameSettings.keyBindJump;
    }


}
