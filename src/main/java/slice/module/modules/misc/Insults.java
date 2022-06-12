package slice.module.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import slice.event.Event;
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

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(target == null)
                return;

            if(target.getHealth() <= 0 || target.isDead) {
                send();
            }
        }
        if(event instanceof EventAttack) {
            EventAttack e = (EventAttack) event;

            if(e.getEntity() instanceof EntityPlayer) target = (EntityPlayer) e.getEntity();
        }
    }

    private void send() {
        if(index >= InsultsClazz.sigma.size())
            index = 0;

        mc.thePlayer.sendChatMessage(InsultsClazz.sigma.get(index));
        index++;
        target = null;
    }
}
