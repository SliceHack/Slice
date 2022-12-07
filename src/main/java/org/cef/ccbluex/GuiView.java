package org.cef.ccbluex;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.ICefRenderer;
import org.cef.browser.lwjgl.CefRendererLwjgl;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.util.LoggerUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@SuppressWarnings("all")
public class GuiView extends GuiScreen {

    private CefBrowserCustom cefBrowser = null;
    private ICefRenderer cefRenderer = null;

    private Map<Integer, Character> pressedKeyMap = new HashMap<>();

    private Page page;

    private boolean transparent = true;
    private int mouseClickY, mouseClickX;

    public GuiView(Page page, boolean transparent) {
        this.page = page;
        this.transparent = transparent;
    }

    public GuiView(Page page) {
        this.page = page;
    }

    @Override
    public void initGui() {
        init();
    }

    public void init() {
        if(cefBrowser != null || cefRenderer != null) {
            destroy();
        }
        cefRenderer = new CefRendererLwjgl(transparent);
        cefBrowser = new CefBrowserCustom(Slice.INSTANCE.getCefRenderManager().getCefClient(), page.getUrl(), transparent, null, cefRenderer);
        cefBrowser.setCloseAllowed();
        cefBrowser.createImmediately();
        cefBrowser.setFocus(true);
        cefBrowser.wasResized_(Display.getWidth(), Display.getHeight());
        Keyboard.enableRepeatEvents(true);
    }

    public void destroy() {
        cefBrowser.close(true);
        Keyboard.enableRepeatEvents(false);
        cefBrowser = null;
        cefRenderer = null;
    }

    @Override
    public void onGuiClosed() {
        destroy();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                cefBrowser.mouseScrolled(Mouse.getX(), Display.getHeight() - Mouse.getY(), keyModifiers(0), 1, wheel);
            }
        }

        // mouse move
        if(mouseClickY == 0 && mouseClickX == 0) {
            cefBrowser.mouseMoved(Mouse.getX(), Display.getHeight() - Mouse.getY(), 0);
        }
        // key up
        HashMap<Integer, Character> pressedKeyMap123 = new HashMap<>();
        pressedKeyMap123.forEach((key, chair) -> {
            new Thread(() -> {
                if (!Keyboard.isKeyDown(key)) {
                    cefBrowser.keyEventByKeyCode(key, chair, keyModifiers(0), false);
                    pressedKeyMap.remove(key);
                }
            }).start();
        });

        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        cefRenderer.render(0.0, 0.0, width, height);
        GlStateManager.enableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onResize(Minecraft p_onResize_1_, int p_onResize_2_, int p_onResize_3_) {
        cefBrowser.wasResized_(Display.getWidth(), Display.getHeight());
        super.onResize(p_onResize_1_, p_onResize_2_, p_onResize_3_);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int key) {
        mouseClickX = Mouse.getX();
        mouseClickY = Mouse.getY();
        cefBrowser.mouseInteracted(Mouse.getX(), Display.getHeight() - Mouse.getY(), mouseModifiers(keyModifiers(0)), key, true, 1);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        cefBrowser.mouseMoved(Mouse.getX(), Display.getHeight() - mouseClickY, mouseModifiers(keyModifiers(0)));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int key) {
        cefBrowser.mouseInteracted(Mouse.getX(), Display.getHeight() - Mouse.getY(), mouseModifiers(keyModifiers(0)), key, false, 1);
        mouseClickY = 0;
        mouseClickX = 0;
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            char charr = Keyboard.getEventCharacter();
            int key = Keyboard.getEventKey();
            int mod = keyModifiers(0);

            cefBrowser.keyEventByKeyCode(key, charr, mod, true);
            pressedKeyMap.put(key, charr);

            if (ChatAllowedCharacters.isAllowedCharacter(charr)) {
                cefBrowser.keyTyped(charr, mod, key);
            }
            keyTyped(charr, key);
        }

        mc.dispatchKeypresses();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static int keyModifiers(int mod) {
        int n = mod;
        if (isCtrlKeyDown()) n = mod | 128;
        if (isShiftKeyDown()) n |= 64;
        if (isAltKeyDown()) n |= 512;
        return n;
    }

    public static int mouseModifiers(int mod) {
        int n = mod;
        if (Mouse.isButtonDown(0)) n = mod | 1024;
        if (Mouse.isButtonDown(2)) n |= 2048;
        if (Mouse.isButtonDown(1)) n |= 4096;
        return n;
    }


}
