package slice.clickgui.component.components.module;

import lombok.Getter;
import lombok.Setter;
import slice.clickgui.component.Component;
import slice.module.Module;

import java.util.List;

@Getter @Setter
public class SettingPane {

    private ModuleButton parent;
    private Module module;
    private int x, y, width, height;

    public SettingPane(ModuleButton button, int x, int y, int width, int height) {
        this.parent = button;
        this.module = button.getModule();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

    }

    public boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
