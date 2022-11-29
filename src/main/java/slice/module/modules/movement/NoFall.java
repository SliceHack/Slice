package slice.module.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.PacketUtil;

@ModuleInfo(name = "NoFall", description = "Spoofs you onGround", category = Category.MOVEMENT)
public class NoFall extends Module {

    ModeValue mode = new ModeValue("Mode", "onGround", "onGround", "Vulcan");

    @EventInfo
    public void onUpdate(EventUpdate e) {
        switch (mode.getValue()) {
            case "onGround":
                e.setOnGround(true);
                break;
            case "Vulcan":
                double mathGround = Math.round(e.getY() / 0.015625) * 0.015625;

                if (mc.thePlayer.fallDistance > 1.3 && mc.thePlayer.ticksExisted % 15 == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mathGround, mc.thePlayer.posZ);
                    e.setY(mathGround);

                    mathGround = Math.round(e.getY() / 0.015625) * 0.015625;
                    if (Math.abs(mathGround - e.getY()) < 0.01) {
                        if (mc.thePlayer.motionY < -0.4) mc.thePlayer.motionY = -0.4;

                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        mc.timer.timerSpeed = 0.9f;
                    }
                } else if (mc.timer.timerSpeed == 0.9f) {
                    mc.timer.timerSpeed = 1;
                }
                break;
        }
    }

}
