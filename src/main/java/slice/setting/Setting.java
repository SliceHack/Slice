package slice.setting;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.module.Module;
import slice.util.LoggerUtil;

@Getter @Setter
public class Setting {
    private String name;
    private boolean hidden;
    private Module module;

    public Setting(String name) {
        this.name = name;
    }

    public void setVisible(boolean visible) {
        setHidden(!visible);
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
//        Slice.INSTANCE.clickGui.setHidden(module, this, hidden);
    }

    public void updateSetting(String name, Object value) {
        String args = "\"" + module.getName() + "\"" + "," +
                "\"" + this.name + "\"" + "," +
                "\"" + name + "\"" + "," +
                "\"" + value + "\"";
//        Slice.INSTANCE.clickGui.runOnIFrame("updateSetting(" + args + ")");
    }

}
