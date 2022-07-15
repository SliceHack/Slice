package slice.module.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventEntityRender;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Chams", description = "Set People Through walls", category = Category.RENDER)
public class Chams extends Module {

    @EventInfo
    public void onEventEntityRender(EventEntityRender e) {
        if(e.isPre()) {
            GL11.glEnable(32823);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GL11.glPolygonOffset(1.0F, -1100000.0F);
            return;
        }
        GL11.glDisable(32823);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GL11.glPolygonOffset(1.0F, 1100000.0F);
    }

}
