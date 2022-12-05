package slice.gui.alt;

import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;

import java.util.ArrayList;
import java.util.List;

public class HTMLAlt extends GuiView {
    List<String> toExec = new ArrayList<>();

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

    public void runJS(String js) {
        toExec.add(js);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        getCefBrowser().mcefUpdate();
        super.drawScreen(mouseX, mouseY, partialTicks);
        toExec.forEach(js -> {
            getCefBrowser().executeJavaScript(js, null, 0);
        });
        toExec.clear();
    }

}
