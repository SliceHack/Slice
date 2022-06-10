package slice.clickgui.setting.settings;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import slice.Slice;
import slice.clickgui.setting.SettingComponent;
import slice.font.TTFFontRenderer;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;

/**
 * Boolean Button
 *
 * @author Nick
 * */
@Getter @Setter
public class BooleanButton extends SettingComponent {

    private BooleanValue booleanValue;

    public BooleanButton(Setting setting, int x, int y) {
        super(setting);
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        font.drawString(setting.getName(), x, y, -1);
        Gui.drawRect((x+5), y, (x+5) + width, y + height, booleanValue.getValue() ? 0xFF00FF00 : 0xFFFF0000);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= (x+5) && mouseX <= (x+5) + width && mouseY >= y && mouseY <= y + height) {
            booleanValue.setValue(!booleanValue.getValue());
        }
    }
}
