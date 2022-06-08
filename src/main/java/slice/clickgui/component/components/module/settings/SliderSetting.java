package slice.clickgui.component.components.module.settings;

import lombok.Getter;
import slice.clickgui.component.Component;
import slice.setting.settings.NumberValue;
import slice.util.RenderUtil;

/**
 * Number Setting for the ClickGui
 *
 * @author Nick
 * */
@Getter
public class SliderSetting extends Component {

    private final NumberValue value;
    private final Number min, max;

    public SliderSetting(NumberValue value, int x, int y, int width, int height) {
        super(value.getName(), x, y, width, height);
        this.value = value;
        this.min = value.getMin();
        this.max = value.getMax();
    }

    /*
     * github copilot carried on math here:
     */
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        double value = this.value.getValue().doubleValue();
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double percent = (value - min) / (max - min);
        int sliderX = (int) (this.getX() + this.getWidth() * percent);
        int sliderY = this.getY() + this.getHeight() / 2;

        RenderUtil.drawRoundedRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 10, 0xFF000000);
        RenderUtil.drawRoundedRect(sliderX - 5, sliderY - 5, sliderX + 5, sliderY + 5, 10, 0xFF000000);
        RenderUtil.drawRoundedRect(sliderX - 3, sliderY - 3, sliderX + 3, sliderY + 3, 10, 0xFFFFFFFF);
    }

    /**
     * Sets the value of the setting.
     * */
    public void setValue(Number value) {
        this.value.setValue(value);
    }
}
