package com.sliceclient.anticheat;

import com.sliceclient.anticheat.user.User;
import com.sliceclient.anticheat.user.UserManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;

/**
 * The Anti-Cheat class.
 *
 * @author Nick
 * */
@Getter
public enum SliceAC {
    INSTANCE;

    private final UserManager userManager;

    /**
     * Constructor.
     * */
    SliceAC() {
        userManager = new UserManager();
        Slice.INSTANCE.getEventManager().register(this);
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        // using objects to avoid slowdowns
        new UpdateUserList();
        new UpdateRemoveUserList();
    }

    public class UpdateUserList {

        public UpdateUserList() {
            for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if(entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    if(player == Minecraft.getMinecraft().thePlayer) return;

                    if(!userManager.hasPlayer(player)) {
                        userManager.addUser(player);
                    }
                }
            }
        }

    }

    public class UpdateRemoveUserList {

        public UpdateRemoveUserList() {
            for(User user : userManager.users) {
                if(!Minecraft.getMinecraft().theWorld.loadedEntityList.contains(user.getPlayer())) {
                    userManager.remove(user);
                }
            }
        }

    }
}
