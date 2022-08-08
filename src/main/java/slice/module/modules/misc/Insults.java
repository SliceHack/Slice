package slice.module.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventAttack;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.module.modules.misc.insults.InsultsClazz;
import slice.setting.settings.ModeValue;

@ModuleInfo(name = "Insults", description = "After getting a kill it sends a message in chat", category = Category.MISC)
public class Insults extends Module {

    ModeValue mode = new ModeValue("Mode", "Sigma", "Sigma");

    private int index;

    private EntityPlayer target;

    @EventInfo
    public void onUpdate(EventUpdate e) {

        if(mc.thePlayer.getHealth() <= 0 || mc.thePlayer.isDead) { target = null; return; }

        if(target == null)
            return;

        if(target.getHealth() <= 0 || target.isDead) {
            send();
        }
    }

    @EventInfo
    public void onAttack(EventAttack e) {

        if(e.getEntity() instanceof EntityPlayer) target = (EntityPlayer) e.getEntity();
    }

    private void send() {
        if(index >= InsultsClazz.sigma.size())
            index = 0;

        mc.thePlayer.sendChatMessage(InsultsClazz.sigma.get(index));
        index++;
        target = null;
    }
}
