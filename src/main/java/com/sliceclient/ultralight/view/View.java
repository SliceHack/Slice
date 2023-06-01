package com.sliceclient.ultralight.view;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmap;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.*;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.math.IntRect;
import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.listener.SliceLoadListener;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class View {

    @Getter
    private final UltralightView view;

    @Getter
    private final List<CustomLoadListener> customLoadListeners = new ArrayList<>();

    @Getter
    private int glTexture = -1;

    @Getter
    private long lastJavascriptGarbageCollections;

    public View() {
        view = UltraLightEngine.getInstance().getRenderer().createView(
                (long) UltraLightEngine.getInstance().getUltraLightEvents().getWidth(), (long) UltraLightEngine.getInstance().getUltraLightEvents().getHeight(),
                new UltralightViewConfig()
                        .initialDeviceScale(1.0)
                        .isTransparent(true)
        );

        view.setLoadListener(
                new SliceLoadListener() {
                    @Override
                    public void onWindowObjectReady(long frameId, boolean isMainFrame, String url) {
                        customLoadListeners.forEach(customLoadListener -> customLoadListener.onWindowObjectReady(frameId, isMainFrame, url));
                    }
                }
        );

        view.focus();
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
            createGLTexture();
        }

        UltralightBitmapSurface surface = (UltralightBitmapSurface) this.view.surface();
        UltralightBitmap bitmap = surface.bitmap();

        int width = (int) view.width();
        int height = (int) view.height();

        // Prepare OpenGL for 2D textures and bind our texture
        glEnable(GL_TEXTURE_2D);

        GlStateManager.bindTexture(this.glTexture);


        IntRect dirtyBounds = surface.dirtyBounds();

        if(dirtyBounds.isValid()) {
            ByteBuffer imageData = bitmap.lockPixels();
            glPixelStorei(GL_UNPACK_ROW_LENGTH, (int) bitmap.rowBytes() / 4);
            if(dirtyBounds.width() == width && dirtyBounds.height() == height) {
                // Update full image
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, imageData);
                glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
            } else {
                // Update partial image
                int x = dirtyBounds.x();
                int y = dirtyBounds.y();
                int dirtyWidth = dirtyBounds.width();
                int dirtyHeight = dirtyBounds.height();
                int startOffset = (int) ((y * bitmap.rowBytes()) + x * 4);

                glTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        x, y, dirtyWidth, dirtyHeight,
                        GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV,
                        (ByteBuffer) imageData.position(startOffset));
            }
            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);

            bitmap.unlockPixels();
            surface.clearDirtyBounds();
        }

        // Set up the OpenGL state for rendering of a fullscreen quad
        glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, view.width(), view.height(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();

        // Disable lighting and scissoring, they could mess up th renderer
        glLoadIdentity();
        glDisable(GL_LIGHTING);
        glDisable(GL_SCISSOR_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Make sure we draw with a neutral color
        // (so we don't mess with the color channels of the image)
        glColor4f(1, 1, 1, 1f);

        glBegin(GL_QUADS);

        // Lower left corner, 0/0 on the screen space, and 0/0 of the image UV
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        // Upper left corner
        glTexCoord2f(0, 1);
        glVertex2i(0, height);

        // Upper right corner
        glTexCoord2f(1, 1);
        glVertex2i(width, height);

        // Lower right corner
        glTexCoord2f(1, 0);
        glVertex2i(width, 0);

        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);

        // Restore OpenGL state
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_TEXTURE_2D);
        glPopAttrib();
    }

    private void createGLTexture() {
        glEnable(GL_TEXTURE_2D);
        this.glTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.glTexture);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    public void eval(String js) {
        try {
            view.evaluateScript(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gc() {
        JavascriptContextLock context = view.lockJavascriptContext();
        try {
            context.getContext().garbageCollect();
        } catch (Exception ignored) {}
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

    public void update() {
        UltraLightEngine.getInstance().getRenderer().update();
        UltraLightEngine.getInstance().getRenderer().render();

        if(lastJavascriptGarbageCollections == 0) {
            lastJavascriptGarbageCollections = System.currentTimeMillis();
            return;
        }

        if(System.currentTimeMillis() - lastJavascriptGarbageCollections > 200) {
            this.lastJavascriptGarbageCollections = System.currentTimeMillis();
            this.gc();
        }

    }

    public void onMouseClick(int x, int y, int mouseButton, boolean buttonDown) {
        UltralightMouseEvent event = new UltralightMouseEvent();
        UltralightMouseEventButton button;
        switch (mouseButton) {
            case 0:
                button = UltralightMouseEventButton.LEFT;
                break;
            case 1:
                button = UltralightMouseEventButton.RIGHT;
                break;
            case 3:
            default:
                button = UltralightMouseEventButton.MIDDLE;
                break;

        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        event.button(button);
        event.x(x * scaledResolution.getScaleFactor());
        event.y(y * scaledResolution.getScaleFactor());
        event.type(buttonDown ? UltralightMouseEventType.DOWN : UltralightMouseEventType.UP);

        view.fireMouseEvent(event);
    }

    interface CustomLoadListener {
        void onWindowObjectReady(long frameId, boolean isMainFrame, String url);
    }

}