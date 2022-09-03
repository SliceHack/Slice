package slice.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;
import slice.event.data.EventInfo;
import slice.event.events.EventEntityRender;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "ESP", description = "Allows you to see entities through walls", category = Category.MISC)
public class ESP extends Module {

    @EventInfo
    public void onEntityRender(EventEntityRender e) {

        double x = e.getEntity().lastTickPosX + (e.getEntity().posX - e.getEntity().lastTickPosX) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosX;
        double y = e.getEntity().lastTickPosY + (e.getEntity().posY - e.getEntity().lastTickPosY) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosY;
        double z = e.getEntity().lastTickPosZ + (e.getEntity().posZ - e.getEntity().lastTickPosZ) * (double)e.getPartialTicks() - (mc.getRenderManager()).viewerPosZ;
        float width = e.getEntity().width;
        float height = e.getEntity().height;


    }

    public void renderBox(int x, int y, int z, float width, float height, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {

    }
}
