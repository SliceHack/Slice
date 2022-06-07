package slice.gui.alt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.font.TTFFontRenderer;
import the_fireplace.ias.gui.GuiPasswordField;

import java.awt.*;
import java.io.IOException;

public class AltLogin extends GuiMainMenu {

    GuiScreen parent;

    public AltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void updateScreen() {
        Gui.drawRect(0, 0, this.width, this.height, new Color(1, 0, 0).getRGB());
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }
}
