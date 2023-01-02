package slice.cef;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.ICefRenderer;
import org.cef.browser.lwjgl.CefRendererLwjgl;
import org.cef.mcef.Page;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.event.events.EventGuiRender;
import slice.gui.hud.legacy.HUD;

import java.util.HashMap;
import java.util.Map;

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
        if(!Slice.INSTANCE.getModuleManager().getModule(HUD.hudClass).isEnabled()) return;
        if(Minecraft.getMinecraft() == null) return;
        if(Minecraft.getMinecraft().theWorld == null) return;
        if(Minecraft.getMinecraft().thePlayer == null) return;

        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        cefRenderer.render(0.0, 0.0, e2d.getWidth(), e2d.getHeight());
        GlStateManager.enableDepth();
    }

    public void onResize(Minecraft p_onResize_1_, int p_onResize_2_, int p_onResize_3_) {
        cefBrowser.wasResized_(p_onResize_2_, p_onResize_3_);
    }

}
