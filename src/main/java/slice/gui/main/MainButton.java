package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;

import java.awt.*;

public class MainButton extends GuiButton {

    public MainButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public MainButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        //draw clean button
        int fontHeight = 50;
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", fontHeight);
        font.drawCenteredString(this.displayString, this.xPosition + this.width / 2f, this.yPosition + this.height / 2f - (fontHeight - 30), new Color(0, 0, 0).getRGB());
        RenderUtil.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 5, -1);

    }
}