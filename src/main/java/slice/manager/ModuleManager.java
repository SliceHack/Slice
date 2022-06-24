package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.modules.combat.AntiBot;
import slice.module.modules.combat.Aura;
import slice.module.modules.combat.PvPBot;
import slice.module.modules.misc.*;
import slice.module.modules.movement.*;
import slice.module.modules.render.Animations;
import slice.module.modules.render.Chams;
import slice.module.modules.render.HUD;
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
        register(new Scaffold());
        register(new Sprint());
        register(new Insults());
        register(new Translator());
        register(new HUD());
        register(new Minehut());
        register(new Chams());
        register(new Animations());
        register(new PvPBot());
    }

    /**
     * Registers a module.
     */
    public void register(Module module) {
        modules.add(module);
    }

    /**
     * Gets modules by category.
     * */
    public List<Module> getModules(Category category) {
        return modules.stream().filter(module -> module.getCategory().equals(category)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets Module By class.
     * */
    public Module getModule(Class<? extends Module> clazz) {
        return modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);
    }

    /**
     * Gets Module By name.
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
}
