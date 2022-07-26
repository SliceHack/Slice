package slice.script.lang.util;

import slice.util.MoveUtil;

/**
 * Player movement utility class
 *
 * @author Nick
 * */
@SuppressWarnings("all")
public class ScriptMoveUtil {

    /** instance */
    public static ScriptMoveUtil INSTANCE = new ScriptMoveUtil();

    /**
     * Checks if the player is moving
     * */
    public boolean isMoving() {
        return MoveUtil.isMoving();
    }

    /**
     * Stops the player from moving
     * */
    public void stop() {
        MoveUtil.stop();
    }

    /**
     * Sets a player's speed
     *
     * @parma speed - the speed and friction to apply to the player
     **/
    public void strafe(final double speed) {
        MoveUtil.strafe(speed);
    }

    /**
     * Gets the player's bps
     * */
    public double getBPS() {
        return MoveUtil.getBPS();
    }

    /**
     * Jumps the player
     * without Minecraft accelerating to high
     */
    public void jump() {
        MoveUtil.jump();
    }

    /**
     * @see #strafe(double)
     */
    public void strafe() {
        MoveUtil.strafe();
    }

    /**
     * Gets how fast a player is moving
     * */
    public double getSpeed() {
        return MoveUtil.getSpeed();
    }

    /**
     * Checks if the player is collied
     * */
    public static boolean isCollided() {
        return MoveUtil.isCollided();
    }
}
