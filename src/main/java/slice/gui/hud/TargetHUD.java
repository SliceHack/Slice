package slice.gui.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

@Getter @Setter
public class TargetHUD {

    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int mouseX, int mouseY) {
        EntityLivingBase target = (EntityLivingBase) mc.pointedEntity;
        if (target == null) {
            return;
        }

        RenderUtil.drawRoundedRect(200, 100, 100, 10, -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {
    }
}
