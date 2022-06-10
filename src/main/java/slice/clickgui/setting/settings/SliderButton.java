package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.setting.SettingComponent;
import slice.font.TTFFontRenderer;
import slice.setting.settings.NumberValue;
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
        if(dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        width = 80;
        height = 10;

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        font.drawString(value.getName(), x, y - 4, -1);
        font.drawString(value.getValue().doubleValue() + "", x + font.getWidth(value.getName()), y - 4, -1);

        // draw off dragX and dragY
        RenderUtil.drawRoundedRect(x - 2, y + font.getHeight(value.getName())-3,  (x - 2) + width, (y+font.getHeight(value.getName())-3) + height, 5, new Color(128, 155, 128).getRGB());
        RenderUtil.drawRoundedRect(x - 2, y + font.getHeight(value.getName())-3, (x - 2) + (int) (width * (value.getValue().doubleValue() / value.getMax().doubleValue())), (y+font.getHeight(value.getName())-3) + height, 5, new Color(255, 155, 255).getRGB());

        if(dragging) {
            value.setValue(value.getMax().doubleValue() * ((x - 2) + (int) (width * (value.getValue().doubleValue() / value.getMax().doubleValue())) - x) / width);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        if(mouseX >= x - 2 && mouseX <= x - 2 + width && mouseY >= y + font.getHeight(value.getName()) && mouseY <= y + font.getHeight(value.getName()) + height) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
    }
}
