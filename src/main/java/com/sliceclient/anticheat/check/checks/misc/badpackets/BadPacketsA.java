package com.sliceclient.anticheat.check.checks.misc.badpackets;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;

@CheckInfo(name = "BadPackets", description = "Detects if a player's pitch is greater than 90.")
public class BadPacketsA extends Check {

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent e) {
        float pitch = Math.abs(user.getPlayer().rotationPitch);

        if(pitch > 90) {
            flag();
        }
    }
}
