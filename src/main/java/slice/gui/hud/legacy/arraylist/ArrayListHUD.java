package slice.gui.hud.legacy.arraylist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.gui.hud.legacy.HUD;
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
            String text =  module.getMode() == null ? module.getName() : module.getName() + " §7" + module.getMode().getValue();
            font.drawStringWithShadow(text, sr.getScaledWidth() - font.getWidth(text) - 12, 10 + i, -1);
            i += font.getHeight(text) + 2;
        }
    }


    /**
     * Gets the enabled modules.
     * @param font - the font to sort the modules by
     */
    public static List<Module> getEnabledModules(TTFFontRenderer font) {
        return Slice.INSTANCE.getModuleManager().getModules()
                .stream()
                .filter((module) -> module.isEnabled() && Slice.INSTANCE.getModuleManager().getModule(HUD.hudClass) != module)
                .sorted((m1, m2) -> (int) (font.getWidth(m2.getMode() == null ? m2.getName() : m2.getName() + " §7" + m2.getMode().getValue()) - font.getWidth(m1.getMode() == null ? m1.getName() : m1.getName() + " §7" + m1.getMode().getValue())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

}

