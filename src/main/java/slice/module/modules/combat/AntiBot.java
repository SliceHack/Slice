package slice.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import net.minecraft.entity.Entity;

@ModuleInfo(name = "AntiBot", description = "Removes all of the bots from the game.", category = Category.COMBAT)
public class AntiBot extends Module {


    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == mc.thePlayer || !entity.isInvisible()) return;

                mc.theWorld.removeEntity(entity);
            }
        }
    }
}
