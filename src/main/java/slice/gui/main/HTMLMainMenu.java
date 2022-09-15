package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.lwjgl.CefRendererLwjgl;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import slice.Slice;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class HTMLMainMenu extends GuiView {

    public HTMLMainMenu() {
        super(new Page(Minecraft.getMinecraft().mcDataDir + File.separator +  "Slice" + File.separator +  "html" + File.separator +  "gui" + File.separator + "main" + File.separator + "index.html"));
    }

    @Override
    public void updateScreen() {
        getCefBrowser().wasResized_(Display.getWidth(), Display.getHeight());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int key) {
        super.mouseClicked(mouseX, mouseY, key);
    }

    /**
     * Prevents cef from destroying the gui
     * */
    @Override
    public void onGuiClosed() {}
}
