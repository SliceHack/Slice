package slice.legacy.clickgui.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.legacy.clickgui.pane.SettingPane;
import slice.font.TTFFontRenderer;
import slice.module.Module;

import java.awt.*;

/**
 * Module Button
 *
 * @author Nick
 * */
@Getter @Setter
@Deprecated
public class ModuleButton {

    /** The Module */
    private Module module;

    private SettingPane pane;

    /** Data */
    private int x, y, width, height;
    private boolean open;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawButton(int mouseX, int mouseY) {
        pane = new SettingPane(module, x + Slice.INSTANCE.getLegacyClickGui().getPane(module.getCategory()).getWidth(), y, width);
        open = Slice.INSTANCE.getLegacyClickGui().isVisible(module);

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        font.drawString(module.getName(), x, y, !module.isEnabled() ? -1 : new Color(255,171,171).getRGB());
        GlStateManager.popMatrix();

        if(open) pane.drawPane(mouseX, mouseY);
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(open) pane.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if((mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height)) {

            if(mouseButton == 0)
                module.toggle();

            if(mouseButton == 1) {
                Slice.INSTANCE.getLegacyClickGui().setVisible(module, !Slice.INSTANCE.getLegacyClickGui().isVisible(module));
            }

        }
        if(open) pane.mouseClicked(mouseX, mouseY, mouseButton);
    }


}
