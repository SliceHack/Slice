package slice.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.setting.Setting;

@Getter @Setter
public class BooleanValue extends Setting {
    private boolean value;

    public BooleanValue(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
        Slice.INSTANCE.getSaver().save();
    }

    /**
     * Lombock's Getters made booleans is a bit annoying, so this is a workaround
     */
    public boolean getValue() {
        return value;
    }
}
