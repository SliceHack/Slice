package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.UltralightSurface;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmap;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.UltralightKeyEvent;
import com.labymedia.ultralight.input.UltralightMouseEvent;
import com.labymedia.ultralight.input.UltralightScrollEvent;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.math.IntRect;
import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sun.scenario.effect.ImageData;
import lombok.Getter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.cef.misc.IntRef;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class View {

    @Getter
    private UltralightView view;

    @Getter
    private int glTexture = -1;

    public View() {
        view = UltraLightEngine.getInstance().getRenderer().createView(
                (long)UltraLightEngine.getInstance().getUltraLightEvents().getWidth(), (long)UltraLightEngine.getInstance().getUltraLightEvents().getHeight(),
                new UltralightViewConfig()
                        .initialDeviceScale(1.0)
                        .isTransparent(true)
        );
    }

    public void loadURL(String url) {
        view.loadURL(url);
    }

    public void loadPage(Page page) {
        loadURL(page.getUrl());
    }

    public void resize(long width, long height) {
        view.resize(width, height);
    }

    public void render() {
        if(glTexture == -1) {
            glTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, glTexture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        UltralightBitmapSurface surface = (UltralightBitmapSurface) view.surface();
        UltralightBitmap bitmap = surface.bitmap();

        int width = (int)view.width(), height = (int)view.height();

        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(glTexture);

        IntRect dirtyBounds = surface.dirtyBounds();

        if(dirtyBounds.isValid()) {
            ByteBuffer imageData = bitmap.lockPixels();

            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
            glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
            glPixelStorei(GL_UNPACK_ROW_LENGTH, (int)bitmap.rowBytes() / 4);

            if (dirtyBounds.width() == width && dirtyBounds.height() == height) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, imageData);
            } else {
                int x = dirtyBounds.x(), y = dirtyBounds.y(), w = dirtyBounds.width(), h = dirtyBounds.height(),
                        startOffset = (int)(y * bitmap.rowBytes() + x * 4);

                glTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        x, y, x, y,
                        GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV,
                        (ByteBuffer) imageData.position(startOffset)
                );
            }

            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);

            bitmap.unlockPixels();
            surface.clearDirtyBounds();
        }

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f,
                (int)UltraLightEngine.getInstance().getUltraLightEvents().getScaledWidth(),
                (int)UltraLightEngine.getInstance().getUltraLightEvents().getScaledHeight(),

                (float) UltraLightEngine.getInstance().getUltraLightEvents().getScaledWidth(),
                (float)UltraLightEngine.getInstance().getUltraLightEvents().getScaledHeight()
        );
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void gc() {
        JavascriptContextLock context = view.lockJavascriptContext();
        try {
            context.getContext().garbageCollect();
        } catch (Exception ignored){}
    }

    public void close() {
        view.unfocus();
        view.stop();

        glDeleteTextures(glTexture);
        glTexture = -1;
    }

    public void fireScrollEvent(UltralightScrollEvent event) {
        view.fireScrollEvent(event);
    }

    public void fireMouseEvent(UltralightMouseEvent event) {
        view.fireMouseEvent(event);
    }

    public void fireKeyEvent(UltralightKeyEvent event) {
        view.fireKeyEvent(event);
    }
}
