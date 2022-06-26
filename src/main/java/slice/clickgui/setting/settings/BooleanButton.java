package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.pane.SettingPane;
import slice.clickgui.setting.SettingComponent;
import slice.font.TTFFontRenderer;
import slice.setting.settings.BooleanValue;
import slice.util.RenderUtil;

import java.awt.*;

/**
 * Boolean Button
 *
 * @author Nick
 * */
@Getter @Setter
public class BooleanButton extends SettingComponent {

    private BooleanValue booleanValue;

    public BooleanButton(SettingPane parent, BooleanValue setting, int x, int y) {
        super(parent, setting, setting.getName());
        this.booleanValue = setting;
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        text = booleanValue.getName();

        font.drawString(setting.getName(), x, y, -1);
        int xAdd = (int) (font.getWidth(setting.getName()))+2;
        RenderUtil.drawRoundedRect(x + xAdd, y, (x+xAdd) + 15, y + 15, 9, booleanValue.getValue() ? new Color(255, 155, 255).getRGB() : new Color(128, 155, 128).getRGB());

        width = (int) (font.getWidth(booleanValue.getName()) + 19);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int xAdd = (int) (Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20).getWidth(setting.getName()))+2;

        if (mouseX >= (x+xAdd) && mouseX <= (x+xAdd) + 15 && mouseY >= y && mouseY <= y + 15) {
            booleanValue.setValue(!booleanValue.getValue());
        }
    }

    public String formatDouble(int places, double value) {
        return String.format("%." + places, value);
    }
}
