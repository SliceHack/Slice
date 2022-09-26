package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.DynamicGuiView;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import slice.Slice;

import java.io.File;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
<<<<<<< HEAD
        super(new Page("https://assets.sliceclient.com/mainmenu/?name=God_Mode"), false);
=======
        super(new Page("https://assets.sliceclient.com/mainmenu/index.html?name=" + Minecraft.getMinecraft().getSession().getUsername()));
>>>>>>> 4b21881937215552a24328a4932532453050266b
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
