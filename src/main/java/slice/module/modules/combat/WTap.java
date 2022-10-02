package slice.module.modules.combat;

import lombok.Getter;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import slice.event.data.EventInfo;
import slice.event.events.EventAttack;
import slice.event.events.EventClientTick;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.PacketUtil;

@ModuleInfo(name = "WTap", description = "Makes people take more knock-back", category = Category.COMBAT)
public class WTap extends Module {

    @Getter
    private int ticks;

    @EventInfo
    public void onAttackEvent(EventAttack event) {
        ticks = 0;
    }

    @EventInfo
    public void onUpdate(EventClientTick e) {
        ticks++;

        if(!mc.thePlayer.isSprinting()) return;
        if (ticks > 10) return;

        PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
    }


}
