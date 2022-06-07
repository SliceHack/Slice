package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

/**
 * Easier rotation methods without doing a lot of math
 *
 * @author Nick
 * */
@UtilityClass
public class RotationUtil {


    /**
     * Gets the direction the player is facing
     */
    public static double getDirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float moveForward = Minecraft.getMinecraft().thePlayer.moveForward;

        if (moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
            else if (moveForward > 0F) forward = 0.5F;

        if (moveForward > 0F) rotationYaw -= 90F * forward;
        if (moveForward < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}
