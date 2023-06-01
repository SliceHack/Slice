package slice.setting;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.module.Module;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

@Getter @Setter
public class Setting {
    protected String name;
    protected Module module;
    protected boolean hidden;

    public Setting(String name) {
        this.name = name;
    }

    public void setVisible(boolean visible) {
        setHidden(!visible);
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getTypeName() {
        if(this instanceof BooleanValue) return "BooleanValue";
        if(this instanceof NumberValue) return "NumberValue";
        if(this instanceof ModeValue) return "ModeValue";

        return "Unknown";
    }

}
