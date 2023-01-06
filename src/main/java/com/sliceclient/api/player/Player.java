package com.sliceclient.api.player;

import com.sliceclient.api.util.Location;
import net.minecraft.client.Minecraft;
import slice.util.MoveUtil;

/**
 * Slice Player API
 *
 * @author Nick
 * @since 1/6/23
 * */
public class Player {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Location location = new Location();

    public void jump() {
        mc.thePlayer.jump();
    }

    public void setOnGround(boolean onGround) {
        mc.thePlayer.onGround = onGround;
    }

    public void strafe(float speed) {
        MoveUtil.strafe(speed);
    }

    public boolean isMoving() {
        return MoveUtil.isMoving();
    }

    public Location getLocation() {
        return location.setX(mc.thePlayer.posX)
                .setY(mc.thePlayer.posY)
                .setZ(mc.thePlayer.posZ)
                .setYaw(mc.thePlayer.rotationYaw)
                .setPitch(mc.thePlayer.rotationPitch);
    }
}
