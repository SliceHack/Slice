package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.DynamicGuiView;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import slice.Slice;

import java.io.File;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
        super(new Page("https://assets.sliceclient.com/mainmenu/index.html?name=" + Minecraft.getMinecraft().getSession().getUsername()));
    }

    /***
     * To reinitialize the gui to prevent memory leaks
     */
    @Override
    public void initGui() {
        init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        getCefBrowser().mcefUpdate();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
