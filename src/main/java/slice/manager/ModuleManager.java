package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.modules.combat.*;
import slice.module.modules.misc.*;
import slice.module.modules.movement.*;
import slice.module.modules.player.*;
import slice.module.modules.render.*;
import slice.module.modules.world.Phase;
import slice.module.modules.world.SumoFences;
import slice.module.modules.world.TimeChanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages all modules.
 *
 * @author Nick
 */
@Getter @Setter
public class ModuleManager {

    /* Every Module */
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        register(new Interface(), new Fly(), new Speed(),
                new InvMove(), new Disabler(), new NoFall(),
                new ChatSpammer(), new Aura(), new NoSlow(),
                new AntiBot(), new TimeChanger(), new Velocity(),
                new AntiCrash(), new Sprint(), new Insults(),
                new Translator(), new HUD(), new Minehut(),
                new Chams(), new Animations(), new PvPBot(),
                new FullBright(), new Derp(), new Reach(),
                new Phase(), new AirJump(), new Step(),
                new Safewalk(), new Botter(), new Spoofer(),
                new SumoFences(), new TargetStrafe(), new AntiVanish(),
                new DavidZarCookieClicker(), new AntiCheat(), new NoBob(),
                new Hat(), new Scaffold(), new AutoPlace(),
                new ESP(), new AutoArmor(), new AutoTool(),
                new WTap(), new AutoPot(), new Stealer(),

                new Manager()
        );
    }

    /**
     * Registers a module.
     *
     * @param module The module to register.
     */
    public void register(Module... module) {
        modules.addAll(Arrays.asList(module));
    }

    /**
     * Unregisters a module.
     *
     * @param module The module to unregister.
     */
    public void unregister(Module module) {
        modules.remove(module);
    }

    /**
     * Gets modules by category.
     *
     * @param category The category to get modules for.
     * */
    public List<Module> getModules(Category category) {
        return modules.stream().filter(module -> module.getCategory().equals(category)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets Module By class.
     *
     * @param clazz The class to get module for.
     * */
    public Module getModule(Class<? extends Module> clazz) {
        return modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);
    }

    /**
     * Gets all enabled modules.
     *
     * @return A list of enabled modules.
     * */
    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isEnabled).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets Module By name.
     *
     * @param name The name to get module for.
     * */
    public Module getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Get Translator
     * */
    public Translator getTranslator() {
        return (Translator) getModule(Translator.class);
    }

    /**
     * Gets MineHut Utility
     * */
    public Minehut getMinehut() {
        return (Minehut) getModule(Minehut.class);
    }

    /**
     * Gets Animations
     * */
    public Animations getAnimations() {
        return (Animations) getModule(Animations.class);
    }

    /**
     * Gets Interface
     */
    public Interface getInterface() {
        return (Interface) getModule(Interface.class);
    }
}
