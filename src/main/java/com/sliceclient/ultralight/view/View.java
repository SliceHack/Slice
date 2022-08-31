package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmap;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.UltralightKeyEvent;
import com.labymedia.ultralight.input.UltralightMouseEvent;
import com.labymedia.ultralight.input.UltralightScrollEvent;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.math.IntRect;
import com.labymedia.ultralight.plugin.loading.UltralightLoadListener;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.listener.TheLoadListener;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

@Getter @Setter
@SuppressWarnings("all")
public class View {
    private UltralightView view;
    private TheLoadListener loadListener;

    private int glTexture = -1;

    public void init() {
        if(view == null) {
            view = UltraLightEngine.INSTANCE.ultraLight();
            view.setLoadListener(loadListener = new TheLoadListener(view));
        }
    }

    public void loadURL(String url) {
        if(view == null) { view = UltraLightEngine.INSTANCE.ultraLight(); view.setLoadListener(loadListener = new TheLoadListener(view)); }
        view.loadURL(url);
    }

    public void resize(int width, int height){
        view.resize(width, height);
    }

    /**
     * @author CCBlueX
     */
    public void render() {
        if (glTexture == -1) {
            createTexture();
        }

        // As we are using the CPU renderer, draw with a bitmap (we did not set a custom surface)
        UltralightBitmapSurface surface = (UltralightBitmapSurface) view.surface();
        UltralightBitmap bitmap = surface.bitmap();
        int width = (int)view.width(), height = (int)view.height();

        // Prepare OpenGL for 2D textures and bind our texture
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(glTexture);

        IntRect dirtyBounds = surface.dirtyBounds();

        if (dirtyBounds.isValid()) {
            ByteBuffer imageData = bitmap.lockPixels();

            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
            glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
            glPixelStorei(GL_UNPACK_ROW_LENGTH, (int)bitmap.rowBytes() / 4);

            if (dirtyBounds.width() == width && dirtyBounds.height() == height) {
                // Update full image
                glTexImage2D(
                        GL_TEXTURE_2D,
                        0,
                        GL_RGBA8,
                        width,
                        height,
                        0,
                        GL_BGRA,
                        GL_UNSIGNED_INT_8_8_8_8_REV,
                        imageData
                );
            } else {
                // Update partial image
                int x = dirtyBounds.x(), y = dirtyBounds.y(), dirtyWidth = dirtyBounds.width(), dirtyHeight = dirtyBounds.height(), startOffset = (int) (y * bitmap.rowBytes() + x * 4);

                glTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        x, y, dirtyWidth, dirtyHeight,
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

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, UltraLightEngine.INSTANCE.getScaledWidth(), UltraLightEngine.INSTANCE.getScaledHeight(), (float)UltraLightEngine.INSTANCE.getScaledWidth(), (float)UltraLightEngine.INSTANCE.getScaledHeight());
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void deleteTexture() {
        glDeleteTextures(glTexture);
        glTexture = -1;
    }

    public void createTexture() {
        glTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, glTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void gc() {
        if(view == null) return;

        JavascriptContextLock lock = view.lockJavascriptContext();
        try {
            lock.getContext().garbageCollect();
        }catch (Throwable ignored){}
    }

    public void close(){
        view.unfocus();
        view.stop();
        deleteTexture();
    }

    public void fireScrollEvent(UltralightScrollEvent event) {
        if(view == null) return;

        view.fireScrollEvent(event);
    }

    public void fireMouseEvent(UltralightMouseEvent event) {
        if(view == null) return;

        view.fireMouseEvent(event);
    }

    public void fireKeyEvent(UltralightKeyEvent event) {
        if(view == null) return;

        view.fireKeyEvent(event);
    }
}
