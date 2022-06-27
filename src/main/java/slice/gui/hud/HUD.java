package slice.gui.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders the client's heads-up-display.
 *
 * @author Nick
 * */
@Getter @Setter
public class HUD {

    @Getter
    @Setter
    private static PlayerOnScreen playerOnScreen = new PlayerOnScreen();

    public static Class<? extends Module> hudClass = slice.module.modules.render.HUD.class;
    private static ModeValue mode;

    public static void draw() {

        /* f3 */
        if (Minecraft.getMinecraft().gameSettings.showDebugProfilerChart || !Slice.INSTANCE.getModuleManager().getModule(hudClass).isEnabled())
            return;

        mode = Slice.INSTANCE.getModuleManager().getModule(hudClass).getMode();
        BooleanValue playerOnScreenBoolean = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("PlayerOnScreen");

        if (playerOnScreenBoolean.getValue()) {
            playerOnScreen.draw(Mouse.getX(), Mouse.getY());
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int widthHeight = ((sr.getScaledHeight() + sr.getScaledWidth()) / 18);

        switch (mode.getValue()) {
            case "Standard":
                drawStandard(sr, widthHeight);
                break;
            case "Smooth":
                drawSmooth(sr, widthHeight);
                break;
        }


        BooleanValue bpsCounter = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("BPS");

        if (bpsCounter.getValue()) {
            TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
            font2.drawStringWithShadow("BPS: " + MoveUtil.getBPS(), 0, sr.getScaledHeight() - font2.getHeight("BPS: " + MoveUtil.getBPS()), -1);
        }
    }

    public static void drawStandard(ScaledResolution sr, int widthHeight) {
        RenderUtil.drawRoundedRect(5, 5, 10 + (widthHeight + 5), 10 + (widthHeight + 5), 10, Integer.MIN_VALUE);

        RenderUtil.drawImage("icons/Slice.png", 10, 10, widthHeight, widthHeight);
        int fontHeight2 = sr.getScaledHeight() / 18;

        ArrayListHUD arrayListHUD = new ArrayListHUD();
        arrayListHUD.draw(fontHeight2);
    }

    public static void drawSmooth(ScaledResolution sr, int widthHeight) {
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
    }

    public static void onTick() {
    }

    public static int getAnimatedColorWave(float seconds, float saturation, float brightness, int index) {
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000) / (seconds / 1000f));
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static int getAnimatedColor(float seconds, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis()) % (int) (seconds * 1000) / (seconds / 1000f));
        return Color.HSBtoRGB(hue, saturation, brightness);
    }
}
