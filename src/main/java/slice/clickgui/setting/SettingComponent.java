package slice.clickgui.setting;

import lombok.Getter;
import lombok.Setter;
import slice.setting.Setting;

@Getter @Setter
public abstract class SettingComponent {

    public Setting setting;
    public int x, y, width, height;

    public SettingComponent(Setting setting) {
        this.setting = setting;
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
}
