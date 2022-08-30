package slice.module.modules.player;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.Event3D;
import slice.event.events.EventEntityRender;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "AutoPlace", description = "Places blocks for you", category = Category.PLAYER)
public class AutoPlace extends Module {

    @EventInfo
    public void onEvent3D(Event3D e) {
        if (this.isHoldingBlock()) {
            mc.rightClickMouse();
        }
    }

    public boolean isHoldingBlock() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().stackSize > 0 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
    }

}