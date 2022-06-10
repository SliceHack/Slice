package slice.clickgui.setting;

import lombok.Getter;
import lombok.Setter;
import slice.setting.Setting;

@Getter @Setter
public abstract class SettingComponent {

    public Setting setting;
    public String text;
    public int x, y, width, height;

    public SettingComponent(Setting setting, String text) {
        this.setting = setting;
        this.text = text;
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
}
