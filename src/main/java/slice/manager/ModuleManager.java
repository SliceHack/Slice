package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.modules.misc.Disabler;
import slice.module.modules.movement.Fly;
import slice.module.modules.movement.InvMove;
import slice.module.modules.movement.NoFall;
import slice.module.modules.movement.Speed;

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
}
