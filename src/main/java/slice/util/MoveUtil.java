package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * Player movement utility class
 *
 * @author Nick
 * */
@UtilityClass
public class MoveUtil {

    /**
     * Checks if the player is moving
     * */
    public boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer != null && (Minecraft.getMinecraft().thePlayer.moveForward != 0.0F || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0F);
    }

    /**
     * Sets a player's speed
     *
     * @parma speed - the speed and friction to apply to the player
     **/
    public void strafe(final double speed) {
        if (!isMoving())
            return;

        final double yaw = RotationUtil.getDirection();
        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }

    /**
     * @see #strafe(double)
     */
    public void strafe() {
        if(Minecraft.getMinecraft().thePlayer.hurtResistantTime > 5)
            return;
        strafe(getSpeed());
    }

    /**
     * Gets how fast a player is moving
     * */
    public double getSpeed() {
        double motionX = Minecraft.getMinecraft().thePlayer.motionX;
        double motionZ = Minecraft.getMinecraft().thePlayer.motionZ;
        return Math.hypot(motionX, motionZ);
    }
}
