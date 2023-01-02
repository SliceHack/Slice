package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.mcef.GuiView;
import org.cef.mcef.Page;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
        super(Page.of("https://assets.sliceclient.com/mainmenu/index.html?name=" + Minecraft.getMinecraft().getSession().getUsername()));
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
