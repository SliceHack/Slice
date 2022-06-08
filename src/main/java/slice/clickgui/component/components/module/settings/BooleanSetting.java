package slice.clickgui.component.components.module.settings;

import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.module.ModuleButton;
import slice.clickgui.component.components.module.SettingPane;
import slice.font.TTFFontRenderer;
import slice.setting.settings.BooleanValue;

/**
 * Boolean Setting for the ClickGui
 *
 * @author Nick
 * */
public class BooleanSetting extends Component {

    SettingPane parent;
    BooleanValue value;

    public BooleanSetting(SettingPane parent, BooleanValue value, int x, int y, int width, int height) {
        super(value.getName(), x, y, width, height);
        this.value = value;
        this.parent = parent;
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
