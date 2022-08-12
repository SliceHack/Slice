package com.sliceclient.anticheat.user;

import com.sliceclient.anticheat.manager.CheckManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The Anti-Cheat User class.
 *
 * @author Nick
 * */
@Getter @Setter
public class User {

    /** The player. */
    private EntityPlayer player;

    /** Violation level */
    private int vl;

    /** CheckManager */
    private CheckManager checkManager;

    public User(EntityPlayer player) {
        this.player = player;
        this.vl = 0;
        this.checkManager = new CheckManager(this);
    }

}
