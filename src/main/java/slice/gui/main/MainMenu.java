package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;
import slice.util.ResourceUtil;

import java.awt.*;
import java.io.IOException;

public class MainMenu extends GuiScreen {

    private int index;

    public void initGui() {
        if(mc.currentScreen != this)
            return;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int x = 240;
        this.buttonList.add(new MainButton(0, x, 200, "Singleplayer", () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        this.buttonList.add(new MainButton(1, x, 260, "Multiplayer", () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
        this.buttonList.add(new MainButton(1, x, (260)+60, "Alt Manager", () -> {}));
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(mc.currentScreen != this)
            return;

        drawBackground();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int fontHeight = sr.getScaledHeight() / 3;
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", fontHeight);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        font.drawCenteredString("Slice", (sr.getScaledWidth() / 2f)+3, 25, -1);
        GlStateManager.popMatrix();

        this.buttonList.forEach(button -> button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY));
    }

    /**
     * Button pressed events
     * */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mc.currentScreen != this)
            return;

        this.buttonList.forEach(button -> {
            if(button instanceof MainButton) {
                MainButton b = (MainButton) button;
                if(b.x <= mouseX && b.x + b.width >= mouseX && b.getY() <= mouseY && b.y + b.height >= mouseY) {
                    b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    b.click();
                }
            }
        });
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, this.width, this.height, new Color(1, 0, 0).getRGB());

        RenderUtil.drawImage("main/background/frame_" + format3Places(index) + ".png", 0, 0,0, 0);
    }

    public String format3Places(int places) {
        if(places < 10) return "00" + places;
        else if(places < 100 && places > 10) return "0" + places;
        else return "" + places;
    }

    public void onTick() {
        if(index >= 215) index = 0;
        else index++;
    }

}
