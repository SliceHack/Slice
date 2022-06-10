package slice.clickgui.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;

import java.awt.*;

/**
 * Module Button
 *
 * @author Nick
 * */
@Getter @Setter
public class ModuleButton {

    /** The Module */
    private Module module;

    /** Data */
    private int x, y, width, height;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawButton(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        font.drawString(module.getName(), x, y, !module.isEnabled() ? -1 : new Color(206, 163, 29).getRGB());
        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if((mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) && mouseButton == 0) {
            module.toggle();
        }
    }


}
