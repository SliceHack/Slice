package slice.clickgui.component.components.module;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.font.TTFFontRenderer;
import slice.module.Module;
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

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(module.getName(), x, y, width, height);
        this.module = module;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 0, new Color(134, 134, 134).getRGB());
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
    }
}
