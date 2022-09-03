package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.modules.combat.*;
import slice.module.modules.misc.*;
import slice.module.modules.movement.*;
import slice.module.modules.player.AutoArmor;
import slice.module.modules.player.AutoPlace;
import slice.module.modules.player.AutoTool;
import slice.module.modules.player.Derp;
import slice.module.modules.player.Scaffold;
import slice.module.modules.render.*;
import slice.module.modules.world.Phase;
import slice.module.modules.world.SumoFences;
import slice.module.modules.world.TimeChanger;

import java.util.ArrayList;
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
        register(new Interface());
        register(new Fly());
        register(new Speed());
        register(new InvMove());
        register(new Disabler());
        register(new NoFall());
        register(new ChatSpammer());
        register(new Aura());
        register(new NoSlow());
        register(new AntiBot());
        register(new TimeChanger());
        register(new Velocity());
        register(new AntiCrash());
        register(new Sprint());
        register(new Insults());
        register(new Translator());
        register(new HUD());
        register(new Minehut());
        register(new Chams());
        register(new Animations());
        register(new PvPBot());
        register(new FullBright());
        register(new Derp());
        register(new Reach());
        register(new Phase());
        register(new AirJump());
        register(new Step());
        register(new Safewalk());
        register(new Botter());
        register(new Spoofer());
        register(new SumoFences());
        register(new TargetStrafe());
        register(new AntiVanish());
        register(new DavidZarCookieClicker());
        register(new AntiCheat());
        register(new NoBob());
        register(new Hat());
        register(new Scaffold());
        register(new AutoPlace());
        register(new ESP());
        register(new AutoArmor());
        register(new AutoTool());
    }

    /**
     * Registers a module.
     *
     * @param module The module to register.
     */
    public void register(Module module) {
        modules.add(module);
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
