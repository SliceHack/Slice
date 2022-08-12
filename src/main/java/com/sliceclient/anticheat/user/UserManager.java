package com.sliceclient.anticheat.user;

import com.sliceclient.anticheat.SliceAC;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserManager class.
 *
 * @author Nick
 * */
public class UserManager {

    /** list */
    public final List<User> users = new ArrayList<>();

    /**
     * Adds a user.
     *
     * @param player the player
     * */
    public void addUser(EntityPlayer player) {
        users.add(new User(player));
    }


    /**
     * Checks if the user has the player.
     *
     * @param player the player
     * @return true if the user manager has the player
     * */
    public boolean hasPlayer(EntityPlayer player) {
        return users.stream().anyMatch(user -> user.getPlayer().equals(player));
    }

    /**
     * Gets a user.
     *
     * @param player the player
     * */
    public User getUser(EntityPlayer player) {
        return users.stream().filter(user -> user.getPlayer().equals(player)).findFirst().orElse(null);
    }

    /**
     * Removes a user.
     *
     * @param user the user
     * */
    public void remove(User user) {
        user.getCheckManager().getChecks().forEach(check -> SliceAC.INSTANCE.getEventManager().unregister(check));
        users.remove(user);
    }
}
