package slice.module.modules.render;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
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
    public void onEntityRender(EventEntityRender event) {
        BlockPos pos = event.getEntity().getPosition();

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x + 1.0, pos.y + 1.0, pos.z + 1.0), Color.WHITE);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
