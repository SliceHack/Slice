package com.sliceclient.anticheat.check.checks.movement.fly;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.check.util.AntiCheatUtil;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;

@CheckInfo(name = "Fly", description = "Detects if a player is fly hacking!")
public class FlyA extends Check {

    private boolean lastOnGround, lastLastOnGround;
    private double lastDistY;

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        double distY = e.getY() - e.getLastY();
        double lastDistY = this.lastDistY;
        this.lastDistY = distY;

        double predictedDist = (lastDistY - 0.08D) * 0.9800000190734863D;

        boolean onGround = AntiCheatUtil.isOnGround(e.getPlayer());
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;
        boolean lastLastOnGround = this.lastLastOnGround;
        this.lastLastOnGround = lastOnGround;

        double abs_distYPredict = Math.abs(distY - predictedDist);

        if (!onGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.005D) {
            if (!(abs_distYPredict < ((user.getPlayer().hurtResistantTime > 2) ? 0.01 : 0.001))) {
                flag();
            }
        }

    }
}