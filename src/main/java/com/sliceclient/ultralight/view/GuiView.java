package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.*;
import com.labymedia.ultralight.javascript.JavascriptEvaluationException;
import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.util.UltraLightUtils;
import com.sliceclient.ultralight.util.UltralightKeyMapper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiView extends GuiScreen {

    @Getter @Setter
    protected View view;

    @Getter @Setter
    protected float lastMouseX = 0, lastMouseY = 0;

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
    public void onResize(Minecraft p_onResize_1_, int p_onResize_2_, int p_onResize_3_) {
        super.onResize(p_onResize_1_, p_onResize_2_, p_onResize_3_);
    }


    @Override
    public void initGui() {
        super.initGui();
    }


    @Override
    public void drawScreen(int x, int y, float p) {
        if(Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            view.getView().fireScrollEvent(
                    new UltralightScrollEvent()
                            .deltaX(x)
                            .deltaY(wheel)
                            .type(UltralightScrollEventType.BY_PIXEL)
            );
        }

        view.render();

        if(lastMouseX != x || lastMouseY != y) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

            view.fireMouseEvent(
                    new UltralightMouseEvent()
                            .x(x * scaledResolution.getScaleFactor())
                            .y(y * scaledResolution.getScaleFactor())
                            .type(UltralightMouseEventType.MOVED)
            );
        }

        if (mc.displayWidth != view.getView().width() || mc.displayHeight != view.getView().height()) {
            view.resize(mc.displayWidth, mc.displayHeight);
        }

        this.lastMouseX = x;
        this.lastMouseY = y;
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        view.onMouseClick(x, y, mouseButton, true);
    }


    @Override
    public void mouseReleased(int x, int y, int mouseButton) {
        view.onMouseClick(x, y, mouseButton, false);
    }

    @Override
    public void keyTyped(char c, int k) {
        UltralightKeyEvent event = new UltralightKeyEvent();
        event.virtualKeyCode(UltralightKeyMapper.getKey(k));
        event.unmodifiedText(String.valueOf(c));

        UltralightKeyMapper.KeyType keyType = UltralightKeyMapper.getKeyType(k);

        if (keyType == UltralightKeyMapper.KeyType.ACTION) {
            event.type(UltralightKeyEventType.RAW_DOWN);

        } else if (keyType == UltralightKeyMapper.KeyType.CHAR) {
            event.type(UltralightKeyEventType.CHAR);
        }

        event.text(String.valueOf(c));
        event.keyIdentifier(UltralightKeyEvent.getKeyIdentifierFromVirtualKeyCode(UltralightKeyMapper.getKey(k)));

        view.fireKeyEvent(event);

        //esc
        if (k == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    public void destroy() {
        UltraLightEngine.getInstance().unregisterView(view);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
