package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;

import java.io.File;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
        super(new Page(Minecraft.getMinecraft().mcDataDir + File.separator +  "Slice" + File.separator +  "html" + File.separator +  "gui" + File.separator + "main" + File.separator + "index.html"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int key) {
        super.mouseClicked(mouseX, mouseY, key);
    }

    /**
     * Prevents cef from destroying the gui
     * */
    @Override
    public void onGuiClosed() {}
}
