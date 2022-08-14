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

        for(User user : userManager.users) {
            boolean checkManagerNull = user.getCheckManager() == null;
            if(checkManagerNull) {
                if(Slice.INSTANCE.getModuleManager().getModule(AntiCheat.class).isEnabled()) {
                    user.setCheckManager(new CheckManager(user));
                }
            }
        }
    }

    @EventInfo
    public void onPacket(EventPacket e) {
    }

    public class UpdateUserList {

        @SuppressWarnings("all")
        public UpdateUserList() {
            for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if(entity instanceof EntityPlayer) {
                    if(Slice.INSTANCE.getModuleManager().getModule(AntiCheat.class).isEnabled()) {
                        boolean hasPlayer = userManager.hasPlayer((EntityPlayer) entity);
                        boolean isNetworkPlayer = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(entity.getUniqueID()) != null;
                        if ((!hasPlayer && entity != Minecraft.getMinecraft().thePlayer) && isNetworkPlayer) {
                            userManager.addUser((EntityPlayer) entity);
                        }
                    }
                }
            }

            try {
                finalize();
            } catch (Throwable ignored){}
        }

    }

    @SuppressWarnings("all")
    public class UpdateRemoveUserList {

        public UpdateRemoveUserList() {
            List<User> users = new ArrayList<>(userManager.users);
            for(User user : users) {
                boolean isNetworkPlayer = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(user.getPlayer().getUniqueID()) != null;

                if(!(Minecraft.getMinecraft().theWorld.loadedEntityList.contains(user.getPlayer()) || user.getPlayer() == Minecraft.getMinecraft().thePlayer) && userManager.hasPlayer(user.getPlayer())) {
                    userManager.remove(user);
                }

                if(!isNetworkPlayer) {
                    userManager.remove(user);
                }

                if(Slice.INSTANCE.getModuleManager().getModule(AntiCheat.class).isEnabled()) {
                    userManager.remove(user);
                }
            }

            try {
                finalize();
            } catch (Throwable ignored){}
        }

    }
}
