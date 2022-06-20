package slice.gui.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;

@Getter @Setter
public class PlayerOnScreen {

    public void draw(int mouseX, int mouseY) {
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

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {
    }
}
