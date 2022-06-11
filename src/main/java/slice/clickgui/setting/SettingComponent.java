package slice.clickgui.setting;

import lombok.Getter;
import lombok.Setter;
import slice.clickgui.pane.SettingPane;
import slice.setting.Setting;

/**
 * Setting Component
 *
 * @author Nick
 * */
@Getter @Setter
public abstract class SettingComponent {

    private SettingPane parent;
    public Setting setting;
    public String text;
    public int x, y, width, height;

    public SettingComponent(SettingPane parent, Setting setting, String text) {
        this.parent = parent;
        this.setting = setting;
        this.text = text;
    }

    public abstract void draw(int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
}
