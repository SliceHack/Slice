package slice.ultralight;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.UltralightKeyEvent;
import com.labymedia.ultralight.input.UltralightMouseEvent;
import com.labymedia.ultralight.input.UltralightScrollEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/***
 * for html rendering we need ultralight
 * this code is not written by us, we converted it from kotlin to java
 *
 * @author CCBlueX || @UnlegitMC
 * */
@Getter @Setter
@SuppressWarnings("all")
public class View {
    private UltralightView view;
    private TheLoadListener loadListener;

    private int glTexture = -1;

    private UltraLightEngine engine;

    public View(UltraLightEngine engine) {
        this.engine = engine;
        view = engine.getRenderer().createView(
                engine.getWidth(), engine.getHeight(),
                new UltralightViewConfig()
                        .initialDeviceScale(1.0)
                        .isTransparent(true)
        );
        loadListener = new TheLoadListener(view);
        view.setLoadListener(loadListener);
    }

    public void loadURL(String url) {
        view.loadURL(url);
    }

    public void resize(long width, long height) {
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
        val surface = (UltralightBitmapSurface) view.surface();
        val bitmap = surface.bitmap();
        val width = (int)view.width();
        val height = (int)view.height();

        // Prepare OpenGL for 2D textures and bind our texture
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(glTexture);

        val dirtyBounds = surface.dirtyBounds();

        if (dirtyBounds.isValid()) {
            val imageData = bitmap.lockPixels();

            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
            glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
            glPixelStorei(GL_UNPACK_ROW_LENGTH, ((int)bitmap.rowBytes()) / 4);

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
                val x = dirtyBounds.x();
                val y = dirtyBounds.y();
                val dirtyWidth = dirtyBounds.width();
                val dirtyHeight = dirtyBounds.height();
                val startOffset = (int)((y * bitmap.rowBytes() + x * 4));

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

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, engine.getScaledWidth(), engine.getScaledHeight(), (float)engine.getScaledWidth(), (float)engine.getScaledHeight());
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
        val lock = view.lockJavascriptContext(); // idk why use{} not working
        try {
            lock.getContext().garbageCollect();
        } catch (Exception ignored){}
    }

    public void close() {
        view.unfocus();
        view.stop();
        deleteTexture();
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