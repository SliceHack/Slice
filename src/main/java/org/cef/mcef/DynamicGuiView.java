package org.cef.mcef;

public class DynamicGuiView extends GuiView {

    public DynamicGuiView(Page page) {
        super(page);
    }

    public DynamicGuiView(Page page, boolean transparent) {
        super(page, transparent);
    }

    public void onGuiClosed() {
        destroy();
    }
}
