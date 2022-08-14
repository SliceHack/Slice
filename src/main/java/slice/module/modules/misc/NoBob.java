package slice.module.modules.misc;

import net.minecraft.client.Minecraft;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "NoBob", description = "Disables ViewBobbing", category = Category.MISC)
public class NoBob extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
        Minecraft.getMinecraft().thePlayer.distanceWalkedModified = 0.0F;
    }
}
