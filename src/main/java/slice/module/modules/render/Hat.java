package slice.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.Event3D;
import slice.event.events.EventEntityRender;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.ColorUtil;
import slice.util.RenderUtil;

import java.awt.*;


@ModuleInfo(name = "Hat", description = "Draws a hat above your head", category = Category.RENDER)
public class Hat extends Module {
    ModeValue mode = new ModeValue("Mode", "Hanabi", "Hanabi");

    NumberValue r = new NumberValue("Red", 0, 0, 255, NumberValue.Type.INTEGER);
    NumberValue g = new NumberValue("Green", 255, 0, 255, NumberValue.Type.INTEGER);
    NumberValue b = new NumberValue("Blue", 255, 0, 255, NumberValue.Type.INTEGER);

    BooleanValue spin = new BooleanValue("Spin", false);

    @EventInfo
    public void onEventEntityRender(EventEntityRender e) {
        if (e.getEntity() != mc.thePlayer) return;
        if (mc.gameSettings.thirdPersonView == 0) return;
        if (e.isPre()) return;
        this.drawHat(e);
    }

    public void drawHat(EventEntityRender e) {
        double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosX;
        double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosY;
        double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosZ;
        int side = 20;
        int stack = 50;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + (mc.thePlayer.isSneaking() ? 2.0 : 2.2), z);
        GL11.glRotatef(-mc.thePlayer.width, 0.0F, 1.0F, 0.0F);
//      set the color using GLStateManager to rainbow
        RenderUtil.glColor(new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));
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
        if(spin.getValue()) GL11.glRotatef(System.currentTimeMillis() % 360, 0.0F, 0.0F, 1.0F);
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
