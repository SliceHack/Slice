package slice.setting;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.module.Module;

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
        Slice.INSTANCE.clickGui.setHidden(module.getName(), name, hidden);
    }
}
