package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;

import java.util.ArrayList;
import java.util.List;

public class HUD {

    public static void draw() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", 60);
        TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);

        if(Minecraft.getMinecraft().gameSettings.showDebugProfilerChart)
            return;

        // title
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        font.drawStringWithShadow("Slice", 10, 10, -1);
        GlStateManager.popMatrix();


        // modules
        List<Module> modules = Slice.INSTANCE.getModuleManager().getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted((m1, m2) -> (int) (font2.getWidth(m1.getName()) - font2.getWidth(m2.getName())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        int y = 5;
        for(Module module : modules) {
            font2.drawStringWithShadow(module.getName(), (sr.getScaledWidth() - font2.getWidth(module.getName()))-5, y, -1);
            y += font2.getHeight(module.getName()) + 5;
        }

    }
}
