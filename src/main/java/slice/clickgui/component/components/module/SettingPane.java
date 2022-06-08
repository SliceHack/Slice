package slice.clickgui.component.components.module;

import lombok.Getter;
import lombok.Setter;
import slice.clickgui.component.Component;
import slice.module.Module;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SettingPane {

    private ModuleButton parent;
    private Module module;
    private int x, y, width, height;

    /** for dragging the settingPane */
    private int dragX, dragY;
    private boolean dragging;

    private List<Component> components = new ArrayList<>();

    public SettingPane(ModuleButton button, int x, int y, int width, int height) {
        this.parent = button;
        this.module = button.getModule();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 25, Color.GRAY.darker().getRGB());

        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
