package slice.gui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.gui.alt.GuiAlt;
import slice.util.RenderUtil;
import viamcp.gui.GuiProtocolSelector;

import java.io.IOException;

public class MainMenu extends GuiScreen {

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int x = 305;
        int y = 200;
        this.buttonList.add(new MainButton(0, x, y, "Singleplayer", () -> mc.displayGuiScreen(new GuiSelectWorld(this))));
        this.buttonList.add(new MainButton(1, x, y+60, "Multiplayer", () -> mc.displayGuiScreen(new GuiMultiplayer(this))));
        this.buttonList.add(new MainButton(2, x, y+120, "Alt Manager", () -> mc.displayGuiScreen(new GuiAlt(this))));
        this.buttonList.add(new MainButton(3, x, y+180, "Version Switcher", () -> mc.displayGuiScreen(new GuiProtocolSelector(this))));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int fontHeight = sr.getScaledHeight() / 3;
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", fontHeight);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Gui.drawCenteredString(font, "Slice", (sr.getScaledWidth() / 2f)+3, 25, -1);
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Button pressed events
     * */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.buttonList.forEach(button -> {
            if(button instanceof MainButton) {
                MainButton b = (MainButton) button;
                if(b.x <= mouseX && b.x + b.width >= mouseX && b.getY() <= mouseY && b.y + b.height >= mouseY) {
                    b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    b.click();
                }
            }
        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    private void drawBackground() {
        RenderUtil.drawImage("main/background/frame_" + format3Places(Slice.INSTANCE.mainIndex) + "_delay-0.03s" + ".png", 0, 0,this.width, this.height);
    }

    public String format3Places(int places) {
        if(places < 10) return "00" + places;
        else if(places == 100) return "100";
        else if(places < 100) return "0" + places;
        else return "" + places;
    }

    public void onTick() {
        if(Slice.INSTANCE.mainIndex >= 215) Slice.INSTANCE.mainIndex = 0;
        else Slice.INSTANCE.mainIndex++;
    }

}