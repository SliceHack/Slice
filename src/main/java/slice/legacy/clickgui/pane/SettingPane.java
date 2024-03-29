package slice.legacy.clickgui.pane;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.legacy.clickgui.setting.SettingComponent;
import slice.legacy.clickgui.setting.settings.BooleanButton;
import slice.legacy.clickgui.setting.settings.ModeButton;
import slice.legacy.clickgui.setting.settings.SliderButton;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
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
@Deprecated
public class SettingPane {

    private Module module;
    private List<SettingComponent> settings = new ArrayList<>();
    private int x, y, width, height;

    public SettingPane(Module module, int x, int y, int width) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        settings.forEach(setting -> setting.mouseReleased(mouseX, mouseY, mouseButton));
    }

    public void drawPane(int mouseX, int mouseY) {
        if(module.getSettings().isEmpty())
            return;

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        settings = new ArrayList<>();

        Slice.INSTANCE.getLegacyClickGui().getWidths().putIfAbsent(module, 0);
        width = Slice.INSTANCE.getLegacyClickGui().getWidths().get(module);

        int yAdd = 0;
        for(Setting setting : module.getSettings()) {
            if(!setting.isHidden()) {
                if (setting instanceof ModeValue) {
                    ModeButton modeButton = new ModeButton(this, (ModeValue) setting, x, y + yAdd);
                    settings.add(modeButton);
                }

                if (setting instanceof BooleanValue) {
                    BooleanButton button = new BooleanButton(this, (BooleanValue) setting, x, y + yAdd);
                    settings.add(button);
                }

                if (setting instanceof NumberValue) {
                    SliderButton sliderButton = new SliderButton(this, (NumberValue) setting, x, y + yAdd);
                    settings.add(sliderButton);
                }
                yAdd += font.getHeight(setting.getName())+5;
            }
        }
        height = yAdd;


        RenderUtil.drawRoundedRect(x, y, x + width, y + (height), 10, new Color(1, 1, 1, 155).getRGB());

        settings.forEach(setting -> setting.draw(mouseX, mouseY));
        Slice.INSTANCE.getLegacyClickGui().setWidth(module, getLargestSetting().getWidth());
    }

    public String formatDouble(int places, double value) {
        return String.format("%.2" + places + "f", value);
    }

    public SettingComponent getLargestSetting() {
        SettingComponent largest = null;
        for(SettingComponent setting : settings) {
            if(largest == null) {
                largest = setting;
            } else {
                if(setting.getWidth() > largest.getWidth()) {
                    largest = setting;
                }
            }
        }
        return largest;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        settings.forEach(setting -> setting.mouseClicked(mouseX, mouseY, mouseButton));
    }

}
