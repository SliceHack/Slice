package com.sliceclient.ultralight.view;

import com.sliceclient.ultralight.Page;

public class DynamicGuiView extends GuiView {

    public DynamicGuiView(Page page) {
        super(page);
    }

    @Override
    public void initGui() {
        init();
    }

    @Override
    public void onGuiClosed() {
        destroy();
    }
}