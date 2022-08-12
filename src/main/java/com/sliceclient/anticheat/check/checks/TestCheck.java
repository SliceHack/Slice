package com.sliceclient.anticheat.check.checks;

import com.sliceclient.anticheat.check.Check;
import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.event.events.PlayerAntiCheatUpdateEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventInfo;

@CheckInfo(name = "TestCheck", description = "Just a test check.")
public class TestCheck extends Check {

    @AntiCheatEventInfo
    public void onPlayerUpdate(PlayerAntiCheatUpdateEvent event) {
//        if(!event.isPre()) return;
//
//        if(user.getPlayer().ticksExisted % 5 != 0) return;
//
//        LoggerUtil.addMessage(event.getPlayer().getName() + " " + user.getPlayer().getName() + " " + SliceAC.INSTANCE.getUserManager().users.size() + " " + this);
    }
}
