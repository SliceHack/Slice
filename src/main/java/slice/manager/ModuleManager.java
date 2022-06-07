package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.module.Module;
import slice.module.modules.movement.Fly;

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
    }

    public void register(Module module) {
        modules.add(module);
    }
}
