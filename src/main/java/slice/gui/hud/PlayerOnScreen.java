package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import slice.util.LoggerUtil;
import slice.util.MathUtil;

public class PlayerOnScreen {

    public void draw() {
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        yaw /= 2.5f;

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GuiInventory.drawEntityOnScreen(120, 80, 25, yaw, 0, Minecraft.getMinecraft().thePlayer);
        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }
}
