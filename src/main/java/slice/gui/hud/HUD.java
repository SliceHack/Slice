package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;

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
        int fontHeight = sr.getScaledHeight() / 6;

    }

    public static List<Module> getEnabledModules(TTFFontRenderer font) {
        return Slice.INSTANCE.getModuleManager().getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted((m1, m2) -> (int) (font.getWidth(m2.getName()) - font.getWidth(m1.getName())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
