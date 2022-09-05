package org.cef.ccbluex;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.optifine.Log;
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

    public GuiView(Page page) {
        this.page = page;

        init();

    }

    public void init() {
        if(cefBrowser != null || cefRenderer != null) {
            destroy();
        }
        cefRenderer = new CefRendererLwjgl(true);
        cefBrowser = new CefBrowserCustom(Slice.INSTANCE.getCefRenderManager().getCefClient(), page.getUrl(), true, null, cefRenderer);
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
        cefBrowser.mouseMoved(Mouse.getX(), Display.getHeight() - Mouse.getY(), 0);

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
        cefBrowser.mouseInteracted(Mouse.getX(), Display.getHeight() - Mouse.getY(), mouseModifiers(keyModifiers(0)), key, true, 1);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        cefBrowser.mouseInteracted(Mouse.getX(), Display.getHeight() - Mouse.getY(), mouseModifiers(keyModifiers(0)), clickedMouseButton, true, 1);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int key) {
        cefBrowser.mouseInteracted(Mouse.getX(), Display.getHeight() - Mouse.getY(), mouseModifiers(keyModifiers(0)), key, false, 1);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            val charr = Keyboard.getEventCharacter();
            val key = Keyboard.getEventKey();
            val mod = keyModifiers(0);
            cefBrowser.keyEventByKeyCode(key, charr, mod, true);
            pressedKeyMap.put(key, charr);

            if (ChatAllowedCharacters.isAllowedCharacter(charr) || key == Keyboard.KEY_RETURN || (int)key == Keyboard.KEY_BACK) {
                switch (key) {
                    case Keyboard.KEY_BACK:
                        cefBrowser.keyTyped((char) 8, mod);
                        break;
                    default:
                        cefBrowser.keyTyped(charr, mod);
                        break;
                }
            }
            keyTyped(charr, key);
        }

        mc.dispatchKeypresses();
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public int keyModifiers(int mod) {
        var n = mod;
        if (isCtrlKeyDown()) n = 0x80;
        if (isShiftKeyDown()) n = 0x40;
        if (isAltKeyDown()) n = 0x200;
        return n;
    }

    public int mouseModifiers(int mod) {
        var n = mod;
        if (Mouse.isButtonDown(0)) n = 0x400;
        if (Mouse.isButtonDown(2)) n = 0x800;
        if (Mouse.isButtonDown(1)) n = 0x1000;
        return n;
    }


}
