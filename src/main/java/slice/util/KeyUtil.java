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
                Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight,
                Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindJump,
                Minecraft.getMinecraft().gameSettings.keyBindSneak,
        };
    }

    /**
     * sneak key
     * */
    public KeyBinding sneak() {
        return Minecraft.getMinecraft().gameSettings.keyBindSprint;
    }

    /**
     * forward key
     * */
    public KeyBinding forward() {
        return Minecraft.getMinecraft().gameSettings.keyBindLeft;
    }

    /**
     * backward key
     * */
    public KeyBinding back() {
        return Minecraft.getMinecraft().gameSettings.keyBindRight;
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
        return Minecraft.getMinecraft().gameSettings.keyBindJump;
    }

    /**
     * jump key
     * */
    public KeyBinding jump() {
        return Minecraft.getMinecraft().gameSettings.keyBindJump;
    }


}
