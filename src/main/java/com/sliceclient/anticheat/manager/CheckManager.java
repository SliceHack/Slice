package com.sliceclient.anticheat.manager;

import com.sliceclient.anticheat.SliceAC;
import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.checks.misc.badpackets.BadPacketsA;
import com.sliceclient.anticheat.check.checks.movement.fly.FlyA;
import com.sliceclient.anticheat.check.checks.movement.ground.GroundSpoofA;
import com.sliceclient.anticheat.check.checks.movement.speed.SpeedA;
import com.sliceclient.anticheat.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The CheckManager class
 *
 * @author Nick
 */
@Getter
public class CheckManager {

    /** The list of checks. */
    private final List<Check> checks = new ArrayList<>();

    /**
     * Constructs a new CheckManager.
     */
    public CheckManager(User user) {
        register(new GroundSpoofA());
        register(new SpeedA());
        register(new FlyA());
        register(new BadPacketsA());

        checks.forEach(check -> { check.setUser(user); SliceAC.INSTANCE.getEventManager().register(check, user.getPlayer()); });
    }

    /**
     * Adds a check to the list of checks.
     *
     * @param check The check to add.
     */
    public void register(Check check) {
        checks.add(check);
    }
}
