package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
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

    public ModeButton(ModeValue setting, int x, int y) {
        super(setting);
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        height = (int)font.getHeight(setting.getName() + ": " + modeValue.getValue())+5;
        width = (int)font.getWidth(setting.getName() + ": " + modeValue.getValue())+5;
        font.drawString(setting.getName() + ": " + modeValue.getValue(), x, y, -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            modeValue.cycle();
        }
    }
}
