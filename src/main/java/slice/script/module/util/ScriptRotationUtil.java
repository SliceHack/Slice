package slice.script.module.util;

import net.minecraft.entity.Entity;
import slice.util.RotationUtil;

/**
 * Easier rotation methods without doing alot of math
 *
 * @author Nick
 * */
public class ScriptRotationUtil {

    public static ScriptRotationUtil INSTANCE = new ScriptRotationUtil();

    /**
     * Gets the direction the player is facing
     * */
    public static double getDirection() {
        return RotationUtil.getDirection();
    }

    public boolean isInFov(Entity entity, double fov) {
        return RotationUtil.isInFov(entity, fov);
    }
}
