package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.pane.SettingPane;
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

    public SliderButton(SettingPane parent, NumberValue setting, int x, int y) {
        super(parent, setting, setting.getName() + " " + String.format("%.2f", setting.getValue().doubleValue()));
        width = 90;
        height = 9;
        this.value = setting;
        this.x = x;
        this.y = y;
        this.dragging = Slice.INSTANCE.getClickGui().isDragging(setting);
    }

    public void draw(int mouseX, int mouseY) {

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        int down = new Color(128, 155, 128).getRGB();
        int over = new Color(255, 155, 255).getRGB();

        int yAdd = ((int) font.getHeight(value.getName() + " " + value.getValue()));
        RenderUtil.drawRoundedRect(x, (y+3), x + width, (y+3) + height, 9, down);
        RenderUtil.drawRoundedRect(x, y+3, x + (int) (width * (value.getValue().doubleValue() / value.getMax().doubleValue())), (y+3) + height, 9, over);
        font.drawString(value.getName() + " " + formatDouble3Places(value.getValue().doubleValue()), x+2, (y+2), -1);

        if(dragging) {
            double percent = (double) (mouseX - x) / (double) (100);
            double val = value.getMin().doubleValue() - percent * (value.getMin().doubleValue() - value.getMax().doubleValue());

            if(val < value.getMin().doubleValue()) val = value.getMin().doubleValue();
            else if(val > value.getMax().doubleValue()) val = value.getMax().doubleValue();

            value.setValue(val);
        }

        width += 5;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        int yAdd = ((int) font.getHeight(value.getName() + " " + value.getValue()));

        if(mouseX >= x && mouseX <= x + width && mouseY >= (y+3) && mouseY <= (y+3) + height) {
            Slice.INSTANCE.getClickGui().setDragging(value, true);
            dragX = mouseX;
            dragY = mouseY;
        }
    }

    public String formatDouble3Places(double value) {
        return String.format("%.2f", value);
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        Slice.INSTANCE.getClickGui().setDragging(value, false);
    }
}
