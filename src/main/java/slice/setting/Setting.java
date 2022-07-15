package slice.setting;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Setting {
    private String name;
    private boolean hidden;

    public Setting(String name) {
        this.name = name;
    }

    public void setVisible(boolean visible) {
        setHidden(!visible);
    }
}
