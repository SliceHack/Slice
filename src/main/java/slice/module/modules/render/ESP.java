package slice.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;
import slice.event.data.EventInfo;
import slice.event.events.EventEntityRender;
import slice.event.events.EventRenderEntityModel;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;
import slice.util.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "ESP", description = "Allows you to see entities through walls", category = Category.MISC)
public class ESP extends Module {

    NumberValue r = new NumberValue("Red", 0, 0, 255, NumberValue.Type.INTEGER);
    NumberValue g = new NumberValue("Green", 255, 0, 255, NumberValue.Type.INTEGER);
    NumberValue b = new NumberValue("Blue", 255, 0, 255, NumberValue.Type.INTEGER);
    BooleanValue players = new BooleanValue("Players", true);
    BooleanValue mobs = new BooleanValue("Mobs", true);

    @EventInfo
    public void onEventRenderModel(EventRenderEntityModel e) {
        if(e.getEntity() == mc.thePlayer) return;
        if(e.getEntity().getDistanceToEntity(mc.thePlayer) > e.getMaxRenderDistance()) return;
        if(e.getEntity() instanceof EntityPlayer && !players.getValue()) return;
        if(e.getEntity() instanceof EntityMob && !mobs.getValue()) return;

        RenderUtil.glColor(new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));

        RenderUtil.setupFBO();

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearStencil(0xF);
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        e.model.render(e.getEntity(), e.p_77036_2_, e.p_77036_3_, e.p_77036_4_, e.p_77036_5_, e.p_77036_6_, e.scaleFactor);

        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        e.model.render(e.getEntity(), e.p_77036_2_, e.p_77036_3_, e.p_77036_4_, e.p_77036_5_, e.p_77036_6_, e.scaleFactor);

        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        e.model.render(e.getEntity(), e.p_77036_2_, e.p_77036_3_, e.p_77036_4_, e.p_77036_5_, e.p_77036_6_, e.scaleFactor);

        RenderUtil.glColor(new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        e.model.render(e.getEntity(), e.p_77036_2_, e.p_77036_3_, e.p_77036_4_, e.p_77036_5_, e.p_77036_6_, e.scaleFactor);

        GL11.glPolygonOffset(1.0F, 2000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
        RenderUtil.glColor(new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));
    }
}
