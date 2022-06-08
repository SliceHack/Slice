package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import slice.clickgui.component.Component;
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

    /** the positions of the ClickGui */
    private int x, y, width = 350, height = 350;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, new Color(105, 101, 101).getRGB());
        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY));
    }

    /**
     * So the gui does not pause the game when it is open
     * */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
