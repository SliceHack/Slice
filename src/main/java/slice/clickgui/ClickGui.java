package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import slice.clickgui.component.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ClickGui extends GuiScreen {

    /** the components of the ClickGui*/
    private List<Component> components = new ArrayList<>();

    /** For dragging the ClickGui */
    private int dragX, dragY;

    /** the width and height of the ClickGui */
    private int width, height;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY));
    }
}
