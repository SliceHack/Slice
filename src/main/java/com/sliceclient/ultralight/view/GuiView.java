package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.input.*;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.support.UltraLightUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.cef.ccbluex.Page;
import org.lwjgl.input.Mouse;
import slice.Slice;
import slice.util.LoggerUtil;

import java.io.File;
import java.io.IOException;

@Getter @Setter
public class GuiView extends GuiScreen {

    public Page page;
    public View view;

    private Page pageToLoad;

    public GuiView(Page page) {
        this.page = page;
        this.view = new View();
        this.pageToLoad = page;
    }

    @Override
    public void initGui() {
        view.init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(view == null) return;

        if(pageToLoad != null) {
            view.loadURL(pageToLoad.getUrl());
            pageToLoad = null;
        }

        if(Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if(wheel != 0) {
                view.fireScrollEvent(new UltralightScrollEvent()
                        .deltaX(0)
                        .deltaY(wheel)
                        .type(UltralightScrollEventType.BY_PIXEL));
            }
        }

        // mouse move
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.MOVED)
                .x(mouseX * UltraLightEngine.INSTANCE.getFactor())
                .y(mouseY * UltraLightEngine.INSTANCE.getFactor())
                .button(UltralightMouseEventButton.LEFT));

        view.render();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(view == null) return;

        UltralightMouseEventButton button = UltraLightUtils.getButtonByButtonID(mouseButton);
        if(button == null) return;
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.DOWN)
                .x(mouseX*UltraLightEngine.INSTANCE.getFactor())
                .y(mouseY*UltraLightEngine.INSTANCE.getFactor())
                .button(button));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(view == null) return;

        UltralightMouseEventButton button = UltraLightUtils.getButtonByButtonID(state);
        if(button == null) return;
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.UP)
                .x(mouseX*UltraLightEngine.INSTANCE.getFactor())
                .y(mouseY*UltraLightEngine.INSTANCE.getFactor())
                .button(button));
    }

    public void destroy() {
        if(view == null) return;

        UltraLightEngine.INSTANCE.unregisterView(view);
    }

    /**
     * Loads an HTML File
     */
    public void loadHTML() {
        if(view == null) return;

        view.loadURL(page.getUrl());
    }
}
