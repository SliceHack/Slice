package slice.clickgui.component.components.module.settings;

import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.module.SettingPane;
import slice.font.TTFFontRenderer;
import slice.setting.settings.ModeValue;

/**
 * Mode Setting for the ClickGui
 *
 * @author Nick
 * */
public class ModeSetting extends Component {

    ModeValue mode;
    SettingPane parent;

    public ModeSetting(SettingPane parent, ModeValue mode, int x, int y, int width, int height) {
        super(mode.getName(), x, y, width, height);
        this.mode = mode;
        this.parent = parent;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        font.drawString(mode.getName() + ": " + mode.getValue(), getX(), getY(), -1);
        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY)) {
            mode.cycle();
        }
    }
}
