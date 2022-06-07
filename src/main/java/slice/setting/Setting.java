package slice.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Setting {
    private String name;
    private boolean hidden;

    public Setting(String name) {
        this.name = name;
    }
}
