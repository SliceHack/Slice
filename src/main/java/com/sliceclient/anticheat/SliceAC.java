package com.sliceclient.anticheat;

import com.sliceclient.anticheat.manager.AntiCheatEventManager;
import com.sliceclient.anticheat.user.User;
import com.sliceclient.anticheat.user.UserManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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

    /** managers */
    private final UserManager userManager;
    private final AntiCheatEventManager eventManager;

    /**
     * Constructor.
     * */
    SliceAC() {
        userManager = new UserManager();
        eventManager = new AntiCheatEventManager();
        Slice.INSTANCE.getEventManager().register(this);
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        new UpdateUserList();
        new UpdateRemoveUserList();
    }

    public class UpdateUserList {

        public UpdateUserList() {
            for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if(entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    if(player == Minecraft.getMinecraft().thePlayer) {
                        if(!userManager.hasPlayer(player)) {
                            userManager.remove(userManager.getUser(player));
                        }
                    }

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
