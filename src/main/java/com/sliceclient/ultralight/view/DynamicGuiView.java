package com.sliceclient.ultralight.view;

import com.sliceclient.ultralight.UltraLightEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.cef.ccbluex.Page;

public class DynamicGuiView extends GuiScreen {

    public View view;

    public DynamicGuiView(Page page) {
        view = new View();
        view.loadURL(page.getUrl());
        UltraLightEngine.INSTANCE.registerView(view);
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
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
