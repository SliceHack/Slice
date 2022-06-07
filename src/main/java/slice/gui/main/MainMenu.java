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

    int fontHeight = 70;

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.buttonList.add(new MainButton(0, 0, 0, "Test"));
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
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
