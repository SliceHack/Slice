package com.sliceclient.ultralight.view;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.Page;

public class DynamicGuiView extends GuiView {

    public DynamicGuiView(Page page) {
        super(page);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        view.render();
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        view.resize(w, h);
    }

    @Override
    public void onGuiClosed() {
        destroy();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
