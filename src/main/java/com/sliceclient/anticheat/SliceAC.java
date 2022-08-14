package com.sliceclient.anticheat;

import com.sliceclient.anticheat.manager.AntiCheatEventManager;
import com.sliceclient.anticheat.manager.CheckManager;
import com.sliceclient.anticheat.user.User;
import com.sliceclient.anticheat.user.UserManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.modules.misc.AntiCheat;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

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
        LoggerUtil.addMessage(userManager.users.size() + "");

        for(User user : userManager.users) {
            boolean checkManagerNull = user.getCheckManager() == null;
            if(checkManagerNull) {
                user.setCheckManager(new CheckManager(user));
            }
        }
    }

    @EventInfo
    public void onPacket(EventPacket e) {
    }

    public class UpdateUserList {

        @SuppressWarnings("all")
        public UpdateUserList() {
            for(EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {

                boolean hasPlayer = userManager.hasPlayer(player);
                boolean isNetworkPlayer = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()) != null;

                if(isNetworkPlayer && !hasPlayer && player != Minecraft.getMinecraft().thePlayer) {
                    userManager.addUser(player);
                }
            }
        }

    }

    @SuppressWarnings("all")
    public class UpdateRemoveUserList {

        public UpdateRemoveUserList() {
            for(EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {

                boolean hasPlayer = userManager.hasPlayer(player);
                boolean isNetworkPlayer = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()) != null;

                if(!(isNetworkPlayer && !hasPlayer && player != Minecraft.getMinecraft().thePlayer) || Minecraft.getMinecraft().theWorld.playerEntities.contains(player)) {
                    userManager.remove(player);
                }
            }
        }

    }
}
