package slice.script.lang.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import slice.util.KeyUtil;

/**
 * For Helping with Keys
 *
 * @author Nick
 * */
@SuppressWarnings("all")
public class ScriptKeyUtil {

    /** instance */
    public static ScriptKeyUtil INSTANCE = new ScriptKeyUtil();

    /**
     * returns all move keys
     * */
    public KeyBinding[] moveKeys() {
        return KeyUtil.moveKeys();
    }

    /**
     * forward key
     * */
    public KeyBinding forward() {
        return KeyUtil.forward();
    }

    /**
     * backward key
     * */
    public KeyBinding back() {
        return KeyUtil.back();
    }

    /**
     * left key
     * */
    public KeyBinding left() {
        return KeyUtil.left();
    }

    /**
     * right key
     * */
    public KeyBinding right() {
        return KeyUtil.right();
    }

    /**
     * jump key
     * */
    public KeyBinding jump() {
        return KeyUtil.jump();
    }


}
