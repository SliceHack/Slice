package slice.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.setting.Setting;

@Getter @Setter
public class ModeValue extends Setting {

    private String value;
    private String[] values;

    private int index;

    public ModeValue(String name, String value, String... values) {
        super(name);
        this.value = value;
        this.values = values;
    }

    public void setValue(String value) {
        this.value = value;
        Slice.INSTANCE.getSaver().save();
    }

    public void cycle() {
        if(values.length == 0)
            return;

        index = (index + 1) % values.length;
        value = values[index];
    }
}
