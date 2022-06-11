package slice.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import slice.util.LoggerUtil;

@Getter @Setter
public class Setting {
    private String name;
    private boolean hidden;

    public Setting(String name) {
        this.name = name;
    }
}
