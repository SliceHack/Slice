package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.DynamicGuiView;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;

import java.io.File;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
        super(new Page(new File(Minecraft.getMinecraft().mcDataDir, "Slice/html/gui/main/index.html")), false);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        getCefBrowser().mcefUpdate();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Prevents cef from destroying the gui
     * */
    @Override
    public void onGuiClosed() {}

}
