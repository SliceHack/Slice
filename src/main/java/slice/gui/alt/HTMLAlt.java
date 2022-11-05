package slice.gui.alt;

import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;

public class HTMLAlt extends GuiView {

    public HTMLAlt() {
        super(new Page("https://assets.sliceclient.com/altmanager/index.html"));
    }

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
