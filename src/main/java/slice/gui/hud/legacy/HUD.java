package slice.gui.hud.legacy;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.gui.hud.legacy.arraylist.SmoothArrayListHUD;
import slice.module.Module;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;

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
//            playerOnScreen.draw(Mouse.getX(), Mouse.getY());
        }

        if (targetHUDBoolean.getValue()) {
//            targetHUD.draw(Mouse.getX(), Mouse.getY());
        } else {
//            RequestHandler.hideTargetHUD();
        }

        if (sessionHUDBoolean.getValue()) {
//            sessionHUD.draw(Mouse.getX(), Mouse.getY());
        } else {
//            RequestHandler.hideSessionHUD();
        }


        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int widthHeight = ((sr.getScaledHeight() + sr.getScaledWidth()) / 18);

        BooleanValue bpsCounter = (BooleanValue) Slice.INSTANCE.getModuleManager().getModule(hudClass).getSetting("BPS");

//        RequestHandler.setBPSVisible(bpsCounter.getValue());

        if (bpsCounter.getValue() && (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat))) {
//            RequestHandler.setBPS(MoveUtil.getBPS());
        }
    }

    public static void onTick() {}
}
