package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.LoggerUtil;
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
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.width = sr.getScaledWidth() / 2;
        this.height = sr.getScaledHeight() / 12;
        float fontHeight = sr.getScaledHeight() / 10.5f;
        float radius = (sr.getScaledHeight() + sr.getScaledWidth()) / 50f;

        if (this.xPosition == 0) {
            this.xPosition = 1;
        }

        if (this.yPosition == 0) {
            this.yPosition = 1;
        }

        int yPosition = (sr.getScaledHeight() / 5) * this.yPosition / 100;
        int xPosition = (sr.getScaledWidth() / 9) * this.xPosition / 100;

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", (int)fontHeight);
        RenderUtil.drawRoundedRect(xPosition, yPosition, xPosition + this.width, yPosition + this.height, radius, -1);
        font.drawCenteredString(this.displayString, xPosition + (this.width / 2f),yPosition + (this.height / 2f) - (fontHeight / 2) + 6, new Color(60, 60, 60).getRGB());

    }
}