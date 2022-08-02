package net.ccbluex.liquidbounce.ui.ultralight.view;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface
import com.labymedia.ultralight.config.UltralightViewConfig
import com.labymedia.ultralight.input.UltralightKeyEvent
import com.labymedia.ultralight.input.UltralightMouseEvent
import com.labymedia.ultralight.input.UltralightScrollEvent
import net.ccbluex.liquidbounce.ui.ultralight.UltralightEngine
import net.ccbluex.liquidbounce.ui.ultralight.listener.TheLoadListener
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*
import java.nio.ByteBuffer

class View {
    private UltralightView view;
    private val loadListener:TheLoadListener

    private var glTexture = -1

    init {
        view = UltralightEngine.renderer.createView(
                UltralightEngine.width.toLong(), UltralightEngine.height.toLong(),
                UltralightViewConfig()
                        .initialDeviceScale(1.0)
                        .isTransparent(true)
        )
        loadListener = TheLoadListener(view)
        view.setLoadListener(loadListener)
    }

    fun loadURL(url:String) {
        view.loadURL(url)
    }

    fun resize(width:Int, height:Int) {
        view.resize(width.toLong(), height.toLong())
    }

    /**
     * @author CCBlueX
     */
    fun render() {
        if (glTexture == -1) {
            createTexture()
        }

        // As we are using the CPU renderer, draw with a bitmap (we did not set a custom surface)
        val surface = view.surface() as UltralightBitmapSurface
        val bitmap = surface.bitmap()
        val width = view.width().toInt()
        val height = view.height().toInt()

        // Prepare OpenGL for 2D textures and bind our texture
        GlStateManager.enableTexture2D()
        GlStateManager.bindTexture(glTexture)

        val dirtyBounds = surface.dirtyBounds()

        if (dirtyBounds.isValid) {
            val imageData = bitmap.lockPixels()

            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0)
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0)
            glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0)
            glPixelStorei(GL_UNPACK_ROW_LENGTH, bitmap.rowBytes().toInt() / 4)

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
                )
            } else {
                // Update partial image
                val x = dirtyBounds.x()
                val y = dirtyBounds.y()
                val dirtyWidth = dirtyBounds.width()
                val dirtyHeight = dirtyBounds.height()
                val startOffset = (y * bitmap.rowBytes() + x * 4).toInt()

                glTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        x, y, dirtyWidth, dirtyHeight,
                        GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV,
                        imageData.position(startOffset)as ByteBuffer
                )
            }
            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)

            bitmap.unlockPixels()
            surface.clearDirtyBounds()
        }

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, UltralightEngine.scaledWidth, UltralightEngine.scaledHeight, UltralightEngine.scaledWidth.toFloat(), UltralightEngine.scaledHeight.toFloat())
        glDepthMask(true)
        glDisable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
    }

    fun deleteTexture() {
        glDeleteTextures(glTexture)
        glTexture = -1
    }

    fun createTexture() {
        glTexture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, glTexture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    func gc() {
        val lock = view.lockJavascriptContext() // idk why use{} not working
        try {
            lock.context.garbageCollect()
        } catch (t:Throwable){
            // ignored
        }
    }

    fun close() {
        view.unfocus()
        view.stop()
        deleteTexture()
    }

    fun fireScrollEvent(event:UltralightScrollEvent) {
        view.fireScrollEvent(event)
    }

    fun fireMouseEvent(event:UltralightMouseEvent) {
        view.fireMouseEvent(event)
    }

    fun fireKeyEvent(event:UltralightKeyEvent) {
        view.fireKeyEvent(event)
    }
}