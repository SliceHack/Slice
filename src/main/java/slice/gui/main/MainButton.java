package slice.gui.main;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;

import java.awt.*;

@Getter @Setter
public class MainButton extends GuiButton {

    public int x, y;
    private Runnable action;

    private int timeSpan;

    public MainButton(int buttonId, int x, int y, String buttonText, Runnable action) {
        super(buttonId, x, y, buttonText);
        this.action = action;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.width = sr.getScaledWidth() / 2;
        this.height = sr.getScaledHeight() / 12;
        float fontHeight = sr.getScaledHeight() / 10.5f;
        float radius = (sr.getScaledHeight() + sr.getScaledWidth()) / 50f;

        this.hovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

        if (this.xPosition == 0) {
            this.xPosition = 1;
        }

        if (this.yPosition == 0) {
            this.yPosition = 1;
        }

        int yPosition = (sr.getScaledHeight() / 5) * this.yPosition / 100;
        int xPosition = (sr.getScaledWidth() / 9) * this.xPosition / 100;

        if(timeSpan < 80  && !hovered) {
            timeSpan = 80;
        }

        if(hovered)
            timeSpan += 5;
        else
            timeSpan = 80;

        if(timeSpan >= 200) {
            timeSpan = 200;
        }

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", (int)fontHeight);
        RenderUtil.drawRoundedRect(xPosition, yPosition, xPosition + this.width, yPosition + this.height, radius, !hovered ? -1 : new Color(255, 80, 0).getRGB());
        font.drawCenteredString(this.displayString, xPosition + (this.width / 2f),yPosition + (this.height / 2f) - (fontHeight / 2) + 6, new Color(60, 60, 60).getRGB());
        x = xPosition;
        y = yPosition;
    }

    public void click() {
        action.run();
    }
}