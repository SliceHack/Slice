package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.pane.SettingPane;
import slice.clickgui.setting.SettingComponent;
import slice.font.TTFFontRenderer;
import slice.setting.settings.ModeValue;

/**
 * Mode Button
 *
 * @author Nick
 * */
@Getter @Setter
public class ModeButton extends SettingComponent {

    private ModeValue modeValue;

    public ModeButton(SettingPane parent, ModeValue setting, int x, int y) {
        super(parent, setting, setting.getName() + ": " + setting.getValue());
        this.modeValue = setting;
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        height = (int)font.getHeight(setting.getName() + ": " + modeValue.getValue())+5;
        width = (int)font.getWidth(setting.getName() + ": " + modeValue.getValue())+5;
        text = setting.getName() + ": " + modeValue.getValue();
        font.drawString(text, x, y, -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            if(mouseButton == 0) modeValue.cycle();
            else if(mouseButton == 1) modeValue.cycleBackwards();
        }
    }
}
