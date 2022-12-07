package org.cef.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.security.KeyRep;
import java.util.concurrent.CompletableFuture;

import org.cef.CefClient;
import org.cef.callback.CefDragData;
import org.cef.handler.CefRenderHandler;
import org.cef.handler.CefScreenInfo;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.util.LoggerUtil;

import static org.lwjgl.input.Keyboard.*;

/**
 * CefBrowserOsr but with custom rendering
 * @see CefBrowser_N is fucking package private
 * @author montoyo, Feather Client Team, modified by (Liulihaocai & NickRest)
 */
@SuppressWarnings("all")
public class CefBrowserCustom extends CefBrowser_N implements CefRenderHandler {
    private final ICefRenderer renderer_;
    private boolean justCreated_ = false;
    private final Rectangle browser_rect_ = new Rectangle(0, 0, 1, 1);
    private final Point screenPoint_ = new Point(0, 0);
    private final boolean isTransparent_;
    private final Component dc_ = new Component(){};

    public CefBrowserCustom(CefClient client, String url, boolean transparent, CefRequestContext context, ICefRenderer renderer) {
        this(client, url, transparent, context, renderer, null, null);
    }

    public CefBrowserCustom(CefClient client, String url, boolean transparent, CefRequestContext context, ICefRenderer renderer, CefBrowserCustom parent, Point inspectAt) {
        super(client, url, context, parent, inspectAt);
        this.isTransparent_ = transparent;
        this.renderer_ = renderer;
        Slice.INSTANCE.getCefRenderManager().getBrowsers().add(this);
    }

    public CefBrowserCustom(CefClient client, String url, boolean transparent, CefRequestContext context, ICefRenderer renderer, CefBrowserCustom parent, Point inspectAt, boolean runRequestHandler) {
        super(client, url, context, parent, inspectAt);
        this.isTransparent_ = transparent;
        this.renderer_ = renderer;
        Slice.INSTANCE.getCefRenderManager().getBrowsers().add(this);
    }

    @Override
    public void createImmediately() {
        this.justCreated_ = true;
        this.createBrowserIfRequired(false);
    }

    @Override
    public Component getUIComponent() {
        return this.dc_;
    }

    @Override
    public CefRenderHandler getRenderHandler() {
        return this;
    }

    @Override
    protected CefBrowser_N createDevToolsBrowser(CefClient client, String url, CefRequestContext context, CefBrowser_N parent, Point inspectAt) {
        return new CefBrowserCustom(client, url, this.isTransparent_, context, null, this, inspectAt);
    }

    private synchronized long getWindowHandle() {
        return 0L;
    }

    @Override
    public Rectangle getViewRect(CefBrowser browser) {
        return this.browser_rect_;
    }

    @Override
    public Point getScreenPoint(CefBrowser browser, Point viewPoint) {
        Point screenPoint = new Point(this.screenPoint_);
        screenPoint.translate(viewPoint.x, viewPoint.y);
        return screenPoint;
    }

    @Override
    public void onPopupShow(CefBrowser browser, boolean show) {
        if (!show) {
            this.renderer_.onPopupClosed();
            this.invalidate();
        }
    }

    @Override
    public void onPopupSize(CefBrowser browser, Rectangle size) {
        this.renderer_.onPopupSize(size);
    }

    private static class PaintData {
        private ByteBuffer buffer;
        private int width;
        private int height;
        private Rectangle[] dirtyRects;
        private boolean hasFrame;
        private boolean fullReRender;
    }

    private final PaintData paintData = new PaintData();

    @Override
    public void onPaint(CefBrowser browser, boolean popup, Rectangle[] dirtyRects,
                        ByteBuffer buffer, int width, int height) {
        if(popup)
            return;

        final int size = (width * height) << 2;

        synchronized(paintData) {
            if(buffer.limit() > size)
                LoggerUtil.addTerminalMessage("Skipping MCEF browser frame, data is too heavy"); //TODO: Don't spam
            else {
                if(paintData.hasFrame) //The previous frame was not uploaded to GL texture, so we skip it and render this on instead
                    paintData.fullReRender = true;

                if(paintData.buffer == null || size != paintData.buffer.capacity()) //This only happens when the browser gets resized
                    paintData.buffer = BufferUtils.createByteBuffer(size);

                paintData.buffer.position(0);
                paintData.buffer.limit(buffer.limit());
                buffer.position(0);
                paintData.buffer.put(buffer);
                paintData.buffer.position(0);

                paintData.width = width;
                paintData.height = height;
                paintData.dirtyRects = dirtyRects;
                paintData.hasFrame = true;
            }
        }
    }

    public void mcefUpdate() {
        if(paintData.hasFrame) {
            renderer_.onPaint(false, paintData.dirtyRects, paintData.buffer, paintData.width, paintData.height, paintData.fullReRender);
            paintData.hasFrame = false;
            paintData.fullReRender = false;
        }
    }

    @Override
    public boolean onCursorChange(CefBrowser browser, int cursorType) {
        return true;
    }

    @Override
    public boolean startDragging(CefBrowser browser, CefDragData dragData, int mask, int x, int y) {
        return false;
    }

    @Override
    public void updateDragCursor(CefBrowser browser, int operation) {
    }

    private void createBrowserIfRequired(boolean hasParent) {
        long windowHandle = 0L;
        if (hasParent) {
            windowHandle = this.getWindowHandle();
        }
        if (this.getNativeRef("CefBrowser") == 0L) {
            if (this.getParentBrowser() != null) {
                this.createDevTools(this.getParentBrowser(), this.getClient(), windowHandle, true, this.isTransparent_, null, this.getInspectAt());
            } else {
                this.createBrowser(this.getClient(), windowHandle, this.getUrl(), true, this.isTransparent_, null, this.getRequestContext());
            }
        } else if (hasParent && this.justCreated_) {
            this.notifyAfterParentChanged();
            this.setFocus(true);
            this.justCreated_ = false;
        }
    }

    private void notifyAfterParentChanged() {
        this.getClient().onAfterParentChanged(this);
    }

    @Override
    public boolean getScreenInfo(CefBrowser browser, CefScreenInfo screenInfo) {
        int depth_per_component = 8;
        int depth = 32;
        double scaleFactor_ = 1.0;
        screenInfo.Set(scaleFactor_, depth, depth_per_component, false, this.browser_rect_.getBounds(), this.browser_rect_.getBounds());
        return true;
    }

    @Override
    public CompletableFuture<BufferedImage> createScreenshot(boolean nativeResolution) {
        return null;
    }

    @Override
    public void close(boolean force) {
        Slice.INSTANCE.getCefRenderManager().getBrowsers().remove(this);
        this.renderer_.destroy();
        super.close(force);
    }

    public void wasResized_(int width, int height) {
        this.browser_rect_.setBounds(0, 0, width, height);
        super.wasResized(width, height);
    }

    public void mouseMoved(int x, int y, int mods) {
        MouseEvent ev = new MouseEvent(dc_, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), mods, x, y, 0, false);
        sendMouseEvent(ev);
    }

    public void mouseInteracted(int x, int y, int mods, int btn, boolean pressed, int ccnt) {
        MouseEvent ev = new MouseEvent(dc_, pressed ? MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), mods, x, y, ccnt, false, remapMouseCode(btn));
        sendMouseEvent(ev);
    }

    private static int remapMouseCode(int kc) {
        switch (kc) {
            case 0: return 1;
            case 1: return 3;
            case 2: return 2;
            default: return 0;
        }
    }

    public void mouseScrolled(int x, int y, int mods, int amount, int rot) {
        MouseWheelEvent ev = new MouseWheelEvent(dc_, MouseEvent.MOUSE_WHEEL, System.currentTimeMillis(), mods, x, y, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, amount, rot);
        sendMouseWheelEvent(ev);
    }

    public void keyTyped(char c, int mods, int key) {
        KeyEvent ev = new KeyEvent(dc_, KeyEvent.KEY_TYPED, System.currentTimeMillis(), mods, lwjlToAWT(key), c);
        sendKeyEvent(ev);
    }

    /**
     * fill the gap between LWJGL and AWT key codes
     * https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list/31637206
     */
    private static int remapKeycode(int kc, char c) {
        switch(kc) {
            case KEY_BACK:   return 8;
            case KEY_DELETE: return 127;
            case KEY_RETURN: return 10;
            case KEY_ESCAPE: return 27;
            case KEY_LEFT:   return 37;
            case KEY_UP:     return 38;
            case KEY_RIGHT:  return 39;
            case KEY_DOWN:   return 40;
            case KEY_TAB:    return 9;
            case KEY_END:    return 35;
            case KEY_HOME:   return 36;
            case KEY_LSHIFT:
            case KEY_RSHIFT:   return 16;
            case KEY_LCONTROL:
            case KEY_RCONTROL:   return 17;
            case KEY_LMENU: // 其实是alt
            case KEY_RMENU:   return 18;

            default: return kc;
        }
    }

    public void keyEventByKeyCode(int keyCode, char c, int mods, boolean pressed) {
        KeyEvent ev = new KeyEvent(dc_, pressed ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED, 0, mods, remapKeycode(keyCode, c), c);
        sendKeyEvent(ev);
    }

    @Override
    protected void finalize() throws Throwable {
        if(!isClosed()) {
            close(true); // NO FUCKING MEMORY LEAKS
        }
        super.finalize();
    }

    private static int lwjlToAWT(int key) {
        switch (key) {
            case KEY_BACK: return 0x08;
            case KEY_TAB: return 0x09;
            case KEY_RETURN: return 0x0A;
            case KEY_ESCAPE: return 0x1B;
            case KEY_DELETE: return 0x7F;
            case KEY_LEFT: return 0x25;
            case KEY_UP: return 0x26;
            case KEY_RIGHT: return 0x27;
            case KEY_DOWN: return 0x28;
            case KEY_PRIOR: return 0x21;
            case KEY_NEXT: return 0x22;
            case KEY_END: return 0x23;
            case KEY_HOME: return 0x24;
            case KEY_INSERT: return 0x2D;
            case KEY_F1: return 0x70;
            case KEY_F2: return 0x71;
            case KEY_F3: return 0x72;
            case KEY_F4: return 0x73;
            case KEY_F5: return 0x74;
            case KEY_F6: return 0x75;
            case KEY_F7: return 0x76;
            case KEY_F8: return 0x77;
            case KEY_F9: return 0x78;
            case KEY_F10: return 0x79;
            case KEY_F11: return 0x7A;
            case KEY_F12: return 0x7B;
            case KEY_F13: return 0xF000;
            case KEY_F14: return 0xF001;
            case KEY_F15: return 0xF002;
            case KEY_F16: return 0xF003;
            case KEY_F17: return 0xF004;
            case KEY_F18: return 0xF005;
            case KEY_F19: return 0xF006;
        }
        return 0;
    }
}
