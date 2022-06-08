package slice.clickgui.component.components.module.settings;

import slice.clickgui.component.Component;
import slice.setting.settings.ModeValue;

public class ModeSetting extends Component {

    ModeValue mode;

    public ModeSetting(ModeValue mode, int x, int y, int width, int height) {
        super(mode.getName(), x, y, width, height);
        this.mode = mode;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY)) {
            mode.cycle();
        }
    }
}
