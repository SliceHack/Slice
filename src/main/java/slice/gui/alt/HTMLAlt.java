package slice.gui.alt;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.DynamicGuiView;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import slice.Slice;

import java.io.File;

public class HTMLAlt extends GuiView {

    public HTMLAlt() {
        super(new Page("https://assets.sliceclient.com/altmanager/index.html"));
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
