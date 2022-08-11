package slice.gui.hud.legacy;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import slice.Slice;
import slice.cef.RequestHandler;
import slice.font.TTFFontRenderer;
import slice.gui.hud.legacy.arraylist.ArrayListHUD;
import slice.gui.hud.legacy.arraylist.SmoothArrayListHUD;
import slice.module.Module;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.util.MoveUtil;
import slice.util.RenderUtil;

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
    @Getter
    @Setter
    private static TargetHUD targetHUD = new TargetHUD();
    private static SessionHUD sessionHUD = new SessionHUD();

    public static Class<? extends Module> hudClass = slice.module.modules.render.HUD.class;
    private static ModeValue mode;

    public static SmoothArrayListHUD smoothArrayListHUD;

    public static void draw() {

        /* f3 */
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo || !Slice.INSTANCE.getModuleManager().getModule(hudClass).isEnabled())
            return;

        mode = Slice.INSTANCE.getModuleManager().getModule(hudClass).getMode();
        BooleanValue playerOnScreenBoolean = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("PlayerOnScreen");
        BooleanValue targetHUDBoolean = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("TargetHUD");
        BooleanValue sessionHUDBoolean = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("SessionHUD");

        if (playerOnScreenBoolean.getValue()) {
            playerOnScreen.draw(Mouse.getX(), Mouse.getY());
        }

        if (targetHUDBoolean.getValue()) {
            targetHUD.draw(Mouse.getX(), Mouse.getY());
        } else {
            RequestHandler.hideTargetHUD();
        }

        if (sessionHUDBoolean.getValue()) {
            sessionHUD.draw(Mouse.getX(), Mouse.getY());
        } else {
            RequestHandler.hideSessionHUD();
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

        if (bpsCounter.getValue() && (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat))) {
            TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
            font2.drawStringWithShadow("BPS: " + MoveUtil.getBPS(), 0, sr.getScaledHeight() - font2.getHeight("BPS: " + MoveUtil.getBPS()), -1);
        }
    }

    public static void drawStandard(ScaledResolution sr, int widthHeight) {
//        RenderUtil.drawRoundedRect(5, 5, 10 + (widthHeight + 5), 10 + (widthHeight + 5), 10, Integer.MIN_VALUE);

        int fontHeight2 = sr.getScaledHeight() / 18;

//        RenderUtil.drawImage("icons/Slice.png", 10, 10, widthHeight, widthHeight);

        ArrayListHUD arrayListHUD = new ArrayListHUD();
        arrayListHUD.draw(fontHeight2);
    }

    public static void drawSmooth(ScaledResolution sr, int widthHeight) {
        try {
//            RenderUtil.drawRoundedRect(5, 5, 10 + (widthHeight + 5), 10 + (widthHeight + 5), 10, Integer.MIN_VALUE);

//            GlStateManager.pushMatrix();
//            GlStateManager.enableTexture2D();
//            GlStateManager.enableBlend();
//            GlStateManager.enableAlpha();
//            RenderUtil.drawImage("icons/Slice.png", 10, 10, widthHeight, widthHeight);
//            GlStateManager.popMatrix();
//            GlStateManager.disableBlend();
//            GlStateManager.disableAlpha();

            int fontHeight2 = sr.getScaledHeight() / 18;

            if (smoothArrayListHUD == null)
                smoothArrayListHUD = new SmoothArrayListHUD();
            smoothArrayListHUD.draw(fontHeight2);
        } catch (Exception ignored){}
    }

    public static void onTick() {
        if(Minecraft.getMinecraft().theWorld == null || smoothArrayListHUD == null || mode.getValue().equalsIgnoreCase("Standard"))
            return;

        smoothArrayListHUD.tick();
    }

}
