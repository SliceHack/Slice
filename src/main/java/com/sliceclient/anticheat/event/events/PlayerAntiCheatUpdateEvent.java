package com.sliceclient.anticheat.event.events;

import com.sliceclient.anticheat.event.AntiCheatEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The PlayerAntiCheatUpdateEvent class.
 * for the anti-cheat system.
 *
 * @author Nick
 * */
@Getter @Setter
public class PlayerAntiCheatUpdateEvent extends AntiCheatEvent {
    private double x, y, z;
    private double lastX, lastY, lastZ;
    private float yaw, pitch;
    private boolean onGround;
    private boolean pre;

    /**
     * Constructor.
     *
     * @param x The x position.
     * @param y The y position.
     * @param z The z position.
     * @param yaw The yaw.
     * @param pitch The pitch.
     * @param onGround The on ground.
     * @param pre The pre.
     * */
    public PlayerAntiCheatUpdateEvent(EntityPlayer player, double x, double y, double z, double lastX, double lastY, double lastZ, float yaw, float pitch, boolean onGround, boolean pre) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.lastX = lastX;
        this.lastY = lastY;
        this.lastZ = lastZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.pre = pre;
    }
}
