package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;

import java.awt.*;
import java.io.IOException;

public class MainMenu extends GuiScreen {

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.buttonList.add(new MainButton(0, 450, 400, "Singleplayer", () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        this.buttonList.add(new MainButton(1, 450, 100, "Multiplayer", () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
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

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.buttonList.forEach(button -> {
            if(button instanceof MainButton) {
                MainButton b = (MainButton) button;
                if(b.x <= mouseX && b.x + b.width >= mouseX && b.getY() <= mouseY && b.y + b.height >= mouseY) {
                    b.click();
                }
            }
        });
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, this.width, this.height, new Color(1, 0, 0).getRGB());
    }
}
