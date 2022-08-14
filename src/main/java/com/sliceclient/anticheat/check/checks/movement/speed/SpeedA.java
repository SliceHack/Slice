package com.sliceclient.anticheat.check.checks.movement.speed;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.check.util.AntiCheatUtil;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;
import slice.util.LoggerUtil;

@CheckInfo(name = "Speed", description = "Detects strafe speed.")
public class SpeedA extends Check {

    private double lastDist;
    private boolean lastOnGround;

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        double dist = ((e.getX() - e.getLastX()) * (e.getX() - e.getLastX())) + ((e.getZ() - e.getLastZ()) * (e.getZ() - e.getLastZ()));
        double lastDist = this.lastDist;
        this.lastDist = dist;

        boolean onGround = AntiCheatUtil.isOnGround(user.getPlayer());
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;

        float friction = 0.91F;
        double shiftedLastDist = lastDist * friction;
        double equalNess = dist - shiftedLastDist;
        double scaledEqualNess = equalNess * 138;

        if(!onGround && !lastOnGround) {
            if(scaledEqualNess >= 0.3) {
                flag();
                LoggerUtil.addMessage("flag " + user.getPlayer().getName());
            }
        }
    }
}
