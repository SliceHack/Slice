package com.sliceclient.anticheat.check.checks;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;

@CheckInfo(name = "TestCheck", description = "Just a test check.")
public class TestCheck extends Check {

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent event) {
//        LoggerUtil.addMessage(user.getPlayer().getName());
    }
}
