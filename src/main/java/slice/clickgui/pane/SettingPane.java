package slice.clickgui.pane;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.setting.SettingComponent;
import slice.clickgui.setting.settings.BooleanButton;
import slice.clickgui.setting.settings.ModeButton;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SettingPane
 *
 * @author Nick
 * */
@Getter @Setter
public class SettingPane {

    private Module module;
    private List<SettingComponent> settings;
    private int x, y, width, height;

    public SettingPane(Module module, int x, int y, int width) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void drawPane(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        height = 180;
        settings = new ArrayList<>();

//        int yAdd = 0;
//        for(Setting setting : module.getSettings()) {
//
//            if(setting instanceof ModeValue) {
//                ModeButton modeButton = new ModeButton((ModeValue)setting, x, y + yAdd);
//                settings.add(modeButton);
//            }
//
//            if(setting instanceof BooleanValue) {
//                BooleanButton button = new BooleanButton(setting, x, y + yAdd);
//                settings.add(button);
//            }
//
//            if(setting instanceof NumberValue) {
//
//            }
//
//            yAdd += font.getHeight(setting.getName())+5;
//        }

        RenderUtil.drawRoundedRect(x, y, width, height, 5, new Color(1, 0, 0, 155).getRGB());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

}
