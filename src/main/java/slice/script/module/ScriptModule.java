package slice.script.module;

import slice.module.Module;
import slice.module.data.Category;

public class ScriptModule extends Module {

    public ScriptModule(String name, Category category) {
        this.name = name;
        this.description = "No description provided.";
        this.category = category;
    }

    public ScriptModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }


}
