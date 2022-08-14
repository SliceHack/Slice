package com.sliceclient.anticheat.check.checks.movement.fly;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.check.util.AntiCheatUtil;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;
import slice.util.LoggerUtil;

@CheckInfo(name = "Fly", description = "Detects if a player is fly hacking!")
public class FlyA extends Check {

    private boolean lastOnGround, lastLastOnGround;
    private double lastDistY;

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        if(user.getAllowFlight()) return;
        if(e.isPre()) return;

        double distY = e.getY() - e.getLastY();
        double lastDistY = this.lastDistY;
        this.lastDistY = distY;

        double predictedDist = (lastDistY - 0.08D) * 0.9800000190734863D;

        boolean isOnGround = AntiCheatUtil.isOnGround(user.getPlayer());

        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = isOnGround;

        boolean lastLastOnGround = this.lastLastOnGround;
        this.lastLastOnGround = lastOnGround;

        double abs_distYPredict = Math.abs(predictedDist - distY);

        if(!isOnGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.005D) {
            LoggerUtil.addMessage(abs_distYPredict + " " + predictedDist);

            if(!(abs_distYPredict > 0.1D)) {
                flag();
            }
        }

    }
}