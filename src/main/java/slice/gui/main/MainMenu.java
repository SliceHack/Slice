package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;

public class MainMenu extends GuiScreen {

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.buttonList.add(new GuiButton(0, 0, 20, 150, 50,"Singleplayer"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int fontHeight = 70;
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", fontHeight);
        font.drawStringWithShadow("Slice", sr.getScaledWidth() / 2f - (fontHeight - 30), 30, -1);
    }
}
