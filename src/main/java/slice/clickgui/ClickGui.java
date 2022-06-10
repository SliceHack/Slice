package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import slice.clickgui.pane.DropdownPane;
import slice.module.Module;
import slice.module.data.Category;

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

    public boolean isVisible(Module module) {
        return openModules.contains(module);
    }

    public void setVisible(Module module, boolean visible) {
        if(visible) openModules.add(module);
        else openModules.remove(module);
    }
}
