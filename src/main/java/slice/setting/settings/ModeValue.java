package slice.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.cef.RequestHandler;
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
        boolean rename = getName().equalsIgnoreCase("Mode") && getModule().isEnabled();

        if(rename) {
            Slice.INSTANCE.getViewHUD().renameFromArrayList(getModule().getName() + " " + getValue(), getModule().getName() + " " + value);
        }

        this.value = value;
        try {
            if (Slice.INSTANCE.getSaver() != null)
                Slice.INSTANCE.getSaver().save();
        }catch (Exception ignored){}

        updateSetting("ModeValue", this.value);
    }

    public void cycle() {
        String e = getName().equalsIgnoreCase("Mode") ? getModule().getName() + " " + getValue() : getModule().getName();

        if(values.length == 0)
            return;

        index = (index + 1) % values.length;
        value = values[index];
        if (Slice.INSTANCE.getSaver() != null) Slice.INSTANCE.getSaver().save();

        if(getModule().isEnabled()) {
            RequestHandler.renameFromArrayList(e, getModule().getName() + " " + getValue());
        }
    }

    public void cycleBackwards() {
        if(values.length == 0)
            return;

        String e = getName().equalsIgnoreCase("Mode") ? getModule().getName() + " " + value : getModule().getName();

        index = (index - 1) % values.length;
        if(index < 0)
            index = values.length - 1;
        value = values[index];
        if (Slice.INSTANCE.getSaver() != null) Slice.INSTANCE.getSaver().save();

        if(getModule().isEnabled()) {
            RequestHandler.renameFromArrayList(e, getModule().getName() + " " + getValue());
        }
    }

}
