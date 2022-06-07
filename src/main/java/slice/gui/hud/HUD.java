package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.util.MoveUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders the client's heads-up-display.
 *
 * @author Nick
 * */
public class HUD {

    public static void draw() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", 60);
        TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        TTFFontRenderer font3 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 15);

        if(Minecraft.getMinecraft().gameSettings.showDebugProfilerChart) {
            GlStateManager.pushMatrix();
            return;
        }

        // title
        font.drawStringWithShadow("Slice", 10, 10, -1);

        // modules
        List<Module> modules = Slice.INSTANCE.getModuleManager().getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted((m1, m2) -> (int) (font2.getWidth(m2.getName()) - font2.getWidth(m1.getName())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        int y = 5;
        for(Module module : modules) {
            font2.drawStringWithShadow(module.getName(), (sr.getScaledWidth() - font2.getWidth(module.getName()))-5, y, -1);
            y += font2.getHeight(module.getName()) + 2;
        }

        font3.drawString("BPS: " + MoveUtil.getBPS(), 0, sr.getScaledHeight() - font3.getHeight("BPS: " + MoveUtil.getBPS()), -1);
    }
}
