package slice.gui.alt.manager.ui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;
import slice.util.Timer;

import java.awt.*;

/**
 * @author Nick
 */
@Getter @Setter
public class AltManButton extends GuiButton {

    public int x, y;
    private Runnable action;

    private boolean pressed, clickAgain = true;
    private Timer timer = new Timer();

    public AltManButton(int buttonId, int x, int y, String buttonText, Runnable action) {
        super(buttonId, x, y, buttonText);
        this.action = action;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.hovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

        this.x = xPosition;
        this.y = yPosition;

        if(this.hovered && Mouse.isButtonDown(0) && clickAgain) {
            this.pressed = !pressed;
            this.clickAgain = false;
            this.click();
        }

        if(!this.clickAgain && !Mouse.isButtonDown(0)) {
            if (this.timer.hasReached(100L)) {
                this.clickAgain = true;
                this.timer.reset();
            }
        }

        RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, 20, 0xFF000000);
    }

    public void click() {
        action.run();
    }
}