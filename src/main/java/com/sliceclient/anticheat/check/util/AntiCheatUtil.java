package com.sliceclient.anticheat.check.util;

import lombok.experimental.UtilityClass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Utility class for anti-cheat checks.
 *
 * @author Nick
 */
@UtilityClass
public class AntiCheatUtil {

    /**
     * Checks if player is on ground.
     *
     * @param player Player to check.
     * */
    public boolean isOnGround(EntityPlayer player) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x += expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(Minecraft.getMinecraft().theWorld.getBlockState(player.getPosition().add(x, -0.5001, z)).getBlock().getMaterial() != Material.air) {
                    return true;
                }
            }
        }
        return false;
    }
}
