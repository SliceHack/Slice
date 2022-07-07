package slice.gui.alt.manager;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.gui.alt.manager.ui.AltButton;
import slice.gui.alt.manager.ui.AltManButton;
import slice.util.RenderUtil;

import java.awt.*;
import java.io.*;

@Getter @Setter
public class AltManager extends GuiScreen {

    private File file = new File(Minecraft.getMinecraft().mcDataDir, "Slice/alts.txt");

    private String[] alts = new String[0];

    GuiScreen parent;

    boolean swap = false;

    public AltManager(GuiScreen screen) {
        this.parent = screen;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(parent);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof AltButton) {
            AltButton altButton = (AltButton) button;
            altButton.click();
        }
        if(button instanceof AltManButton) {
            AltManButton altManButton = (AltManButton) button;
            altManButton.click();
        }
    }


    public void initGui() {
        load();

        int count2 = 0, count = 0;
        int xAdd = 5, yAdd = 2;

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", (20/5)*5);

        for(String alt : alts) {
            this.buttonList.add(new AltButton(alt, count2, xAdd, yAdd, 40, 20));

            if(swap) {
                yAdd += 25;
                swap = false;
            } else {
                xAdd += (font.getWidth(alt) + 2)/6;
                if(xAdd > width / 4f) {
                    swap = true;
                    xAdd = 0;
                }
            }

            count++;
            count2++;
        }
    }

    public void onGuiClosed() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String alt : alts) {
                bw.write(alt);
                bw.newLine();
            }
            bw.close();
        } catch (Exception ignored){}
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();


        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        RenderUtil.drawRoundedRect(0, 0, width / 4f, height, 0, new Color(1, 0, 0, 100).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
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

    public void addAlt(String alt) {
        alts = ArrayUtils.add(alts, alt);
    }

    public void load() {
        try {
            if (!file.exists()) {

                if(file.createNewFile())
                    return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if(line.split(":").length >= 2) sb.append(line).append("\n");
            }
            br.close();
            alts = sb.toString().split("\n");
        } catch (Exception ignored){}
    }
}
