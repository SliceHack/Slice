package slice.gui.hud.legacy;

import com.labymedia.ultralight.UltralightJava;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.ColorUtil;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.UUID;

@Getter @Setter
public class TargetHUD {

    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int mouseX, int mouseY) {
        EntityLivingBase target = Slice.INSTANCE.target;
        if (target == null)
            return;

        int targetYaw = (int) target.rotationYaw;
        targetYaw /= 2.5f;
        String distance = String.format("%.1f", target.getDistanceToEntity(mc.thePlayer));
        String health = String.format("%.1f", target.getHealth());
        String hurtTime = target.hurtTime + "";


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

        GlStateManager.clear(256);
        RenderUtil.drawRoundedRect(borderX, borderY, borderWidth, borderHeight, borderRadius, new Color(255,171,171).getRGB());
        RenderUtil.drawRoundedRect(x, y, width, height, radius, new Color(60, 60, 60).getRGB());

        if (target instanceof EntityPlayer) {
            try {
                RenderUtil.drawHead(target.getUniqueID(), x + 10, y + 16, 65, 65);
            } catch (Exception ignored) {
                RenderUtil.drawHead(UUID.fromString("e107870e-5cd7-469d-b82e-cbdeabff3213"), x + 10, y + 16, 65, 65);
            }
        } else {
            GuiInventory.drawEntityOnScreen(x + 40, y + 95, 40, targetYaw, 0, target, false);
        }

        font.drawStringWithShadow("Name: " + target.getName(), x+80, y - (font.getHeight(distance) / 2) + 25, -1);
        font.drawStringWithShadow("Distance: " + distance, x+80, y - (font.getHeight(distance) / 2) + 40, -1);
        font.drawStringWithShadow("Health: " + health + "\u2764", x+80, y - (font.getHeight(distance) / 2) + 55, -1);
        font.drawStringWithShadow("Hurt: " + hurtTime, x+80, y - (font.getHeight(distance) / 2) + 70, -1);

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {
    }
}
