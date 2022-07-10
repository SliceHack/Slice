package slice.gui.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;

@Getter @Setter
public class TargetHUD {

    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int mouseX, int mouseY) {
        EntityLivingBase target = Slice.INSTANCE.target;
        if (target == null)
            return;

        int targetYaw = (int) target.rotationYaw;
        targetYaw /= 2.5f;
        String distance = String.format("%.2f", target.getDistanceToEntity(mc.thePlayer));
        String health = String.format("%.2f", target.getHealth());
        String hurtTime = String.format("%.0f", (float) target.hurtTime);


        int x = 100;
        int y = 100;
        int width = x + 200;
        int height = y + 100;
        int radius = 10;
        int borderX = x - 2;
        int borderY = y - 2;
        int borderWidth = width + 2;
        int borderHeight = height + 2;
        int borderRadius = 10;


        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        RenderUtil.drawRoundedRect(borderX, borderY, borderWidth, borderHeight, borderRadius, new Color(255,171,171).getRGB());
        GlStateManager.popMatrix();
        RenderUtil.drawRoundedRect(x, y, width, height, radius, new Color(60, 60, 60).getRGB());
        //TODO: remove nametag from player on screen
        GuiInventory.drawEntityOnScreen(x + 40, y + 95, 40, targetYaw, 0, target);

        font.drawStringWithShadow("Distance: " + distance, x+80, y - (font.getHeight(distance) / 2) + 20, -1);
        font.drawStringWithShadow("Health: " + health, x+80, y - (font.getHeight(distance) / 2) + 35, -1);
        font.drawStringWithShadow("Hurte: " + hurtTime, x+80, y - (font.getHeight(distance) / 2) + 50, -1);

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {
    }
}
