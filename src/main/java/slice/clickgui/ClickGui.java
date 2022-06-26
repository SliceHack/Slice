package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import slice.clickgui.pane.DropdownPane;
import slice.clickgui.pane.SettingPane;
import slice.clickgui.setting.settings.SliderButton;
import slice.module.Module;
import slice.module.data.Category;
import slice.setting.Setting;
import slice.setting.settings.NumberValue;

import java.io.IOException;
import java.util.*;

/**
 * ClickGui
 *
 * @author Nick
 * */
@Getter @Setter
public class ClickGui extends GuiScreen {

    private List<DropdownPane> paneList = new ArrayList<>();

    private List<Module> openModules = new ArrayList<>();
    private List<NumberValue> dragging = new ArrayList<>();

    private Map<Module, Integer> widths = new HashMap<>();

    public ClickGui() {
        int xAdd = 0;
        for(Category category : Category.values()) {
            paneList.add(new DropdownPane(category, xAdd, 0));
            xAdd += 60;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        paneList.forEach(pane -> pane.mouseReleased(mouseX, mouseY, state));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        paneList.forEach(pane -> pane.drawPane(mouseX, mouseY));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        paneList.forEach(pane -> pane.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * checks if a module is open
     * @param module the module to check
     * */
    public boolean isVisible(Module module) {
        return openModules.contains(module);
    }

    /**
     * Sets the visibility of a module.
     * @param module The module to set the visibility of.
     * @param visible Whether the module should be visible.
     * */
    public void setVisible(Module module, boolean visible) {
        if(visible) openModules.add(module);
        else openModules.remove(module);
    }

    /**
     * Sets the width of a setting pane.
     * @param pane The pane to set the width of.
     * @param width The width to set the pane to.
     * */
    public void setWidth(Module pane, int width) {
        widths.put(pane, width);
    }

    /**
     * Gets pane by category
     * @param category Category
     * */
    public DropdownPane getPane(Category category) {
        return paneList.stream().filter(pane -> pane.getCategory() == category).findFirst().orElse(null);
    }

    /**
     * Checks dragging state of a slider
     * @param slider Slider to check
     * */
    public boolean isDragging(NumberValue slider) {
        return dragging.contains(slider);
    }

    /**
     * sets the dragging state of a slider
     * @param button the slider button
     * @param dragging true if dragging, false if not
     */
    public void setDragging(NumberValue button, boolean dragging) {
        if(dragging) this.dragging.add(button);
        else this.dragging.remove(button);
    }
}
