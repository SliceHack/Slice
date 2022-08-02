package org.cef.ccbluex;

@SuppressWarnings("all")
public class AllTimeGuiView extends GuiView {

    public AllTimeGuiView(Page page) {
        super(page);
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }
}
