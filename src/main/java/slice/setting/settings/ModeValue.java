package slice.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.setting.Setting;

@Getter @Setter
public class ModeValue extends Setting {

    private String value;
    private String[] values;

    private int index;

    public ModeValue(String name, String... values) {
        super(name);
        this.value = values[0];
        this.values = values;
    }

    public void cycle() {
        if(index >= values.length) index = 0;
        else index++;
        value = values[index];
    }
}
