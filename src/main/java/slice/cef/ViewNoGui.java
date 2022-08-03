package slice.cef;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.ICefRenderer;
import org.cef.browser.lwjgl.CefRendererLwjgl;
import org.cef.ccbluex.Page;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.event.events.Event2D;
import slice.event.events.EventGuiRender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.client.gui.GuiScreen.*;
import static org.lwjgl.opengl.GL11.*;

@Getter @Setter
@SuppressWarnings("all")
public class ViewNoGui {

    public CefBrowserCustom cefBrowser = null;
    public ICefRenderer cefRenderer = null;

    private Map<Integer, Character> pressedKeyMap = new HashMap<>();

    private Page page;

    private boolean init;

    public ViewNoGui(Page page) {
        this.page = page;
    }

    public void init() {
        cefRenderer = new CefRendererLwjgl(true);
        cefBrowser = new CefBrowserCustom(Slice.INSTANCE.getCefRenderManager().getCefClient(), page.getUrl(), true, null, cefRenderer);
        cefBrowser.setCloseAllowed();
        cefBrowser.createImmediately();
        cefBrowser.setFocus(true);
        cefBrowser.wasResized_(Display.getWidth(), Display.getHeight());
        Keyboard.enableRepeatEvents(true);
        init = true;
    }

    public void destroy() {
        try {
            cefBrowser.close(true);
            Keyboard.enableRepeatEvents(false);
            cefBrowser = null;
            cefRenderer = null;
        } catch (Exception ignored){}
    }

    public void draw(EventGuiRender e2d) {
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        cefRenderer.render(0.0, 0.0, e2d.getWidth(), e2d.getHeight());
        GlStateManager.enableDepth();
    }

    public void onResize(Minecraft p_onResize_1_, int p_onResize_2_, int p_onResize_3_) {
        cefBrowser.wasResized_(p_onResize_2_, p_onResize_3_);
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
