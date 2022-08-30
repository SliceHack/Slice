package slice.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.Event3D;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.ColorUtil;
import slice.util.RenderUtil;


@ModuleInfo(name = "Hat", description = "Draws a hat above your head", category = Category.RENDER)
public class Hat extends Module {

    public ModeValue mode = new ModeValue("Mode", "Hanabi", "Hanabi");

    @EventInfo
    public void onEvent3D(Event3D e) {
        if (mc.gameSettings.thirdPersonView == 0) return;
        this.drawHat(e);
    }

    public void drawHat(Event3D e) {
        double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosX;
        double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosY;
        double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosZ;
        int side = 45;
        int stack = 50;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + (mc.thePlayer.isSneaking() ? 2.0 : 2.2), z);
        GL11.glRotatef(-mc.thePlayer.width, 0.0F, 1.0F, 0.0F);
//      set the color using GLStateManager to rainbow
        GlStateManager.color(0, 255, 255, 100);
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(1.0F);
        Cylinder c = new Cylinder();
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        c.setDrawStyle(100011);
        c.draw(0.0F, 0.8F, 0.4F, side, stack);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
    }

}
