package com.sliceclient.anticheat.check.checks.movement.ground;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.check.util.AntiCheatUtil;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;
import net.minecraft.client.Minecraft;

@CheckInfo(name = "Ground", description = "Detects if a player is spoofing their onGround state")
public class GroundSpoofA extends Check {

    private boolean lastOnGround, lastLastOnGround;

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        boolean onGround = AntiCheatUtil.isOnGround(e.getPlayer());

        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;

        boolean lastLastOnGround = this.lastLastOnGround;
        this.lastLastOnGround = lastOnGround;

        if((!onGround && !lastOnGround && !lastLastOnGround) && user.getPlayer().onGround) {
            flag();
        }
    }
}
