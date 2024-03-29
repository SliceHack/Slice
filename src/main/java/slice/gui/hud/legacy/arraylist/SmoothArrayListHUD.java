package slice.gui.hud.legacy.arraylist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.gui.hud.legacy.HUD;
import slice.module.Module;
import slice.util.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class SmoothArrayListHUD {

    Minecraft mc = Minecraft.getMinecraft();

    private List<Module> modules = new ArrayList<>();
    private final Map<Module, Integer> moduleTicks = new HashMap<>();
    private final Map<Module, Timer> moduleTimers = new HashMap<>();

    public SmoothArrayListHUD() {
        Slice.INSTANCE.getModuleManager().getModules().forEach((module) -> {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int fontHeight = sr.getScaledHeight() / 18;
            TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", fontHeight);

            String text = module.getMode() == null ? module.getName() : module.getName() + " §7" + module.getMode().getValue();
            if(module.isEnabled()) {
            }
        });
    }

    public void draw(int fontHeight) {
        ScaledResolution sr = new ScaledResolution(mc);

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", fontHeight);
        modules = getEnabledModules(font);

        int i = 2;
        for (Module module : modules) {
            GlStateManager.pushMatrix();

            String text = module.getMode() == null ? module.getName() : module.getName() + " §7" + module.getMode().getValue();

            i += font.getHeight(text) + 1;
            GlStateManager.popMatrix();

            modules.forEach(module1 -> moduleTicks.put(module1, moduleTicks.getOrDefault(module1, 0) + 1));
        }
    }

    public void tick() {
        modules.forEach((module) -> {
            if(moduleTimers.get(module) == null) {
                moduleTimers.put(module, new Timer());
            }
        });
    }

    public void onToggle(Module module) {
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

