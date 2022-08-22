package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import slice.Slice;
import slice.module.modules.movement.TargetStrafe;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
     * Stops the player from moving
     * */
    public void stop() {
        Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
        Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
        Minecraft.getMinecraft().thePlayer.rotationYaw = Slice.INSTANCE.getServerYaw();
        Minecraft.getMinecraft().thePlayer.rotationPitch = Slice.INSTANCE.getServerPitch();


        for (KeyBinding key : KeyUtil.moveKeys()) key.pressed = false;
    }

    /**
     * Resets the player's motion to 0
     * */
    public void resetMotion(boolean yPosition) {
        Minecraft.getMinecraft().thePlayer.motionX = 0;
        Minecraft.getMinecraft().thePlayer.motionZ = 0;
        if (yPosition)
            Minecraft.getMinecraft().thePlayer.motionY = 0;
    }

    /**
     * Sets a player's speed
     *
     * @parma speed - the speed and friction to apply to the player
     **/
    public void strafe(final double speed) {
        TargetStrafe targetStrafe = (TargetStrafe) Slice.INSTANCE.getModuleManager().getModule(TargetStrafe.class);

        if((Slice.INSTANCE.target != null && targetStrafe.strafing) && targetStrafe.isEnabled()) return;

        if (!isMoving())
            return;

        final double yaw = RotationUtil.getDirection();
        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }

    /**
     * Gets the player's bps
     * */
    public double getBPS() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return MathUtil.round(((Math.hypot(player.posX - player.prevPosX, player.posZ - player.prevPosZ) * Minecraft.getMinecraft().timer.timerSpeed) * 20), 2);
    }

    /**
     * Jumps the player
     * without Minecaft accelerating to high
     */
    public void jump() {
        Minecraft.getMinecraft().thePlayer.motionY = 0.42F;
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

    /**
     * Checks if the player is collied
     * */
    public static boolean isCollided() {
        return Minecraft.getMinecraft().thePlayer.isCollided;
    }
}
