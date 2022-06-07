package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;

import java.awt.*;

public class MainMenu extends GuiScreen {

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.buttonList.add(new MainButton(0, 450, 400, "Singleplayer"));
        this.buttonList.add(new MainButton(1, 450, 100, "Multiplayer"));
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int fontHeight = sr.getScaledHeight() / 3;
        this.drawBackground();
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", fontHeight);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        font.drawCenteredString("Slice", (sr.getScaledWidth() / 2f)-2, 25, -1);
        GlStateManager.popMatrix();

        this.buttonList.forEach(button -> button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY));
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, this.width, this.height, new Color(1, 0, 0).getRGB());
    }
}
