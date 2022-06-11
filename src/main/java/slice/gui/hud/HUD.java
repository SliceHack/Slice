package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;
import slice.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders the client's heads-up-display.
 *
 * @author Nick
 * */
public class HUD {

    public static void onTick() {}

    public static void draw() {

        /* f3 */
        if(Minecraft.getMinecraft().gameSettings.showDebugProfilerChart)
            return;

        PlayerOnScreen playerOnScreen = new PlayerOnScreen();
        playerOnScreen.draw();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int widthHeight = ((sr.getScaledHeight() + sr.getScaledWidth()) / 18);

        RenderUtil.drawRoundedRect(5, 5, 10 + (widthHeight + 5), 10 + (widthHeight + 5), 10, Integer.MIN_VALUE);

        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        RenderUtil.drawImage("icons/Slice.png", 10, 10, widthHeight, widthHeight);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        int fontHeight2 = sr.getScaledHeight() / 18;

        ArrayListHUD arrayListHUD = new ArrayListHUD();
        arrayListHUD.draw(fontHeight2);

        TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        font2.drawStringWithShadow("BPS: " + MoveUtil.getBPS(), 0, sr.getScaledHeight() - font2.getHeight("BPS: " + MoveUtil.getBPS()), -1);
    }

}
