package com.sliceclient.anticheat.check.checks.movement.ground;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.check.util.AntiCheatUtil;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;

@CheckInfo(name = "Ground", description = "Checks if the player is spoofing offGround", type = "B")
public class GroundSpoofB extends Check {

    private boolean lastOnGround, lastLastOnGround;

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        boolean onGround = AntiCheatUtil.isOnGround(e.getPlayer());

        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;

        boolean lastLastOnGround = this.lastLastOnGround;
        this.lastLastOnGround = lastOnGround;

        if((onGround && lastOnGround && lastLastOnGround) && !user.getPlayer().onGround) {
            flag();
        }
    }
}
