package slice.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.setting.Setting;

@Getter @Setter
public class NumberValue extends Setting {
    private Number value, min, max;
    private Type type;

    public NumberValue(String name, Number value, Number min, Number max, Type type) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.type = type;
    }

    public void setValue(Number value) {
        this.value = value;

        if(value.doubleValue() > max.doubleValue()) this.value = max;
        else if(value.doubleValue() < min.doubleValue()) this.value = min;

        Slice.INSTANCE.getSaver().save();
    }

    public enum Type {
        DOUBLE,
        FLOAT,
        INTEGER,
        LONG
    }
}
