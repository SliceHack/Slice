package org.cef.ccbluex;

public class DynamicGuiView extends GuiView {

    public DynamicGuiView(Page page) {
        super(page);
    }

    public void initGui() {
        init();
    }

    public void onGuiClosed() {
        destroy();
    }
}
