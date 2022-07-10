package slice.gui.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import slice.Slice;
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

        int x = 100;
        int y = 100;
        int width = x + 100;
        int height = y + 100;
        int radius = 10;

        RenderUtil.drawRoundedRect(x, y, width, height, radius, -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {
    }
}
