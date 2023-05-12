package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.input.*;
import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.util.UltraLightUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class GuiView extends GuiScreen {

    @Getter
    private View view;

    @Getter @Setter
    private Page page;

    public GuiView(Page page) {
        this.page = page;
    }

    public void init() {
        view = new View();
        view.loadPage(page);
        UltraLightEngine.getInstance().registerView(view);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // mouse stroll
        if(Mouse.hasWheel()){
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
                .x(mouseX* UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .y(mouseY* UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .button(UltralightMouseEventButton.LEFT));

        view.render();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        UltralightMouseEventButton button = UltraLightUtils.getButtonByButtonID(clickedMouseButton);
        if (button == null)
            return;

        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.DOWN)
                .x(mouseX * UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .y(mouseY * UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .button(button));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int key) {
        UltralightMouseEventButton button = UltraLightUtils.getButtonByButtonID(key);
        if (button == null) {
            return;
        }
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.UP)
                .x(mouseX * UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .y(mouseY * UltraLightEngine.getInstance().getUltraLightEvents().getScaledFactor())
                .button(button));
    }

    public void destroy() {
        UltraLightEngine.getInstance().unregisterView(view);
    }
}
