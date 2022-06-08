package slice.clickgui.component.components.module.settings;

import slice.clickgui.component.Component;
import slice.setting.settings.NumberValue;

/**
 * Number Setting for the ClickGui
 *
 * @author Nick
 * */
public class SliderSetting extends Component {

    private NumberValue value;
    private Number min, max;

    public SliderSetting(NumberValue value, int x, int y, int width, int height) {
        super(value.getName(), x, y, width, height);
        this.value = value;
        this.min = value.getMin();
        this.max = value.getMax();
    }

    /**
     * Sets the value of the setting.
     * */
    public void setValue(Number value) {
        this.value.setValue(value);
    }
}
