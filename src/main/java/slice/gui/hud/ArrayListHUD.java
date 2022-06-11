package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ArrayListHUD {

    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int fontHeight) {
        ScaledResolution sr = new ScaledResolution(mc);

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", fontHeight);
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

