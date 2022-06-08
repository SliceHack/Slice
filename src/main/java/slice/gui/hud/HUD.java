package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders the client's heads-up-display.
 *
 * @author Nick
 * */
public class HUD {

    public static void onTick() {

    }

    public static void draw() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        /* f3 */
        if(Minecraft.getMinecraft().gameSettings.showDebugProfilerChart)
            return;

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

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        List<Module> modules = getEnabledModules(font);
        int i = 2;
        for(Module module : modules) {
            font.drawStringWithShadow(module.getName(), sr.getScaledWidth() - font.getWidth(module.getName()) - 12, 10 + i, -1);
            i += font.getHeight(module.getName()) + 2;
        }
    }

    /**
     * Gets the enabled modules.
     * @param font - the font to sort the modules by
     */
    public static List<Module> getEnabledModules(TTFFontRenderer font) {
        return Slice.INSTANCE.getModuleManager().getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted((m1, m2) -> (int) (font.getWidth(m2.getName()) - font.getWidth(m1.getName())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
