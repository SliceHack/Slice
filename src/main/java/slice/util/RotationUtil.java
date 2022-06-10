package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

/**
 * Easier rotation methods without doing alot of math
 *
 * @author Nick
 * */
@UtilityClass
public class RotationUtil {

    /**
     * Gets the direction the player is facing
     * */
    public static double getDirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if (Minecraft.getMinecraft().thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;

        if (Minecraft.getMinecraft().thePlayer.moveForward < 0F) forward = -0.5F;
        else if (Minecraft.getMinecraft().thePlayer.moveForward > 0F) forward = 0.5F;

        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public boolean isInFov(Entity entity, double fov) {
        double xDiff = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double yDiff = entity.posY - Minecraft.getMinecraft().thePlayer.posY;
        double zDiff = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
        double angle = Math.toDegrees(Math.acos(xDiff / distance));
        return angle < fov;
    }
}
