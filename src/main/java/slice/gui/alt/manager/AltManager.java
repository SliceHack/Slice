package slice.gui.alt.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;

@Getter @Setter
@AllArgsConstructor
@Deprecated
public class AltManager extends GuiScreen {

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawBackground();

        RenderUtil.drawRect(0, 0, (width/8)*2, height, Integer.MIN_VALUE);
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", height / 7);
        Gui.drawCenteredString(font, "Alt Manager", (width / 2f), 10, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    private void drawBackground() {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        RenderUtil.drawImage("main/background/frame_" + format3Places(Slice.INSTANCE.mainIndex) + "_delay-0.03s" + ".png", 0, 0,this.width, this.height);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
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
