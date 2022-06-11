package slice.clickgui.pane;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.setting.SettingComponent;
import slice.clickgui.setting.settings.BooleanButton;
import slice.clickgui.setting.settings.ModeButton;
import slice.clickgui.setting.settings.SliderButton;
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
import java.util.Set;

/**
 * SettingPane
 *
 * @author Nick
 * */
@Getter @Setter
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
            }

            yAdd += font.getHeight(setting.getName())+5;
        }
        height = yAdd;


        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        int width = getLargestSetting() != null ? (int)font.getWidth(getLargestSetting().getText())+5 : 80;

        if(getLargestSetting() != null) {
            if (getLargestSetting() instanceof SliderButton) {
                width += getLargestSetting().getWidth()+5;
            }
        }

        RenderUtil.drawRoundedRect(x, y, x + width, y + (height), 15, new Color(1, 0, 0, 155).getRGB());
        GlStateManager.popMatrix();
        settings.forEach(setting -> setting.draw(mouseX, mouseY));
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
