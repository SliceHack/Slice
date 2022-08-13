package com.sliceclient.anticheat.event.events;

import com.sliceclient.anticheat.event.AntiCheatEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiCheatPlayerAnimationEvent extends AntiCheatEvent {

    /**
     * Handles animation packets.
     *
     * @param entityID The entityID to get the player of.
     * */
    public AntiCheatPlayerAnimationEvent(int entityID) {
        for(Entity player : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if(player instanceof EntityPlayer && player.getEntityId() == entityID) {
                this.player = (EntityPlayer) player;
            }
        }
    }
}