package slice.clickgui.component.components.module.settings;

import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.font.TTFFontRenderer;
import slice.setting.settings.BooleanValue;

public class BooleanSetting extends Component {

    BooleanValue value;

    public BooleanSetting(BooleanValue value, int x, int y, int width, int height) {
        super(value.getName(), x, y, width, height);
        this.value = value;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        font.drawString(value.getName() + ": " + value.getValue(), getX(), getY(), -1);
        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY)) {
            value.setValue(!value.getValue());
        }
    }
}
