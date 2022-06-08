package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.CategoryButton;
import slice.util.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Client ClickGui
 *
 * @author Nick & Dylan
 * */
@Getter @Setter
public class ClickGui extends GuiScreen {

    /** the components of the ClickGui*/
    private List<Component> components = new ArrayList<>();

    /** For dragging the ClickGui */
    private int dragX, dragY;
    private boolean dragging;

    /** the positions of the ClickGui */
    private int x, y, width = 350, height = 350;

    /** data */
    private CategoryButton selectedCategory;

    public void initGui() {
        dragging = false;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, new Color(105, 101, 101).darker().getRGB());
        RenderUtil.drawRoundedRect(x - 5, y, x + (width+2), y + 30, 15, new Color(105, 101, 101).darker().darker().getRGB());
        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY));

        if(isInsideOfDrag(mouseX, mouseY)) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    public boolean isInsideOfDrag(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + (width+2) && mouseY >= y && mouseY <= y + 30;
    }

    /**
     * So the gui does not pause the game when it is open
     * */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
