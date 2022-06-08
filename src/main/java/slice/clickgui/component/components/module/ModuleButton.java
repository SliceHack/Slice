package slice.clickgui.component.components.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;

/**
 * Module Button
 *
 * @author Nick
 * */
@Getter @Setter
public class ModuleButton extends Component {

    private Module module;
    private boolean open;

    private final int defualtHeight;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(module.getName(), x, y, width, height);
        this.module = module;
        this.defualtHeight = height;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        RenderUtil.drawRoundedRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 25, Color.GRAY.darker().getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.color(255, 200, 0, 255);

        setHeight(open ? defualtHeight + 20 : defualtHeight);
        int color = !module.isEnabled() ? -1: Color.ORANGE.darker().getRGB();

        boolean hovered = isHovered(mouseX, mouseY);
//        if(hovered) {
//            font.drawString(module.getDescription(), getWidth() - (font.getWidth(module.getDescription())) - 5, getY(), Color.GRAY.getRGB());
//        }

        font.drawString(getName(), getX() + 10, getY(), color);

        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY)) {

            if(mouseButton == 0) {
                module.toggle();
            }

            if(mouseButton == 1) {
                open = !open;

            }
            Slice.INSTANCE.getClickGui().getCategoryButton(module.getCategory()).updateButtons();
        }

    }
}
