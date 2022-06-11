package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.setting.SettingComponent;
import slice.font.TTFFontRenderer;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;

@Getter @Setter
public class SliderButton extends SettingComponent {

    private NumberValue value;

    private int dragX, dragY;
    private boolean dragging;

    public SliderButton(NumberValue setting, int x, int y) {
        super(setting, setting.getName());
        this.value = setting;
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        width = 120;
        height = 10;

        this.width = (int) (width * (value.getValue().doubleValue() / value.getMax().doubleValue()));

        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, new Color(128, 155, 128).getRGB());
        RenderUtil.drawRoundedRect(x, y, x + (int) (width * value.getValue().doubleValue() / value.getMax().doubleValue()), y + height, 10, new Color(255, 155, 255).getRGB());
        if(dragging) {
            LoggerUtil.addMessage(((width - (mouseX - dragX)) * value.getMax().doubleValue() / width) + "");
            value.setValue((width - (mouseX - dragX)) * value.getMax().doubleValue() / width);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            LoggerUtil.addMessage("dragging " + value.getName());
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }
}
