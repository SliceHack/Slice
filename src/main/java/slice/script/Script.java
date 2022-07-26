package slice.script;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.module.ScriptModule;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static slice.module.data.Category.*;

/**
 * The Script class
 *
 * @author Nick
 * */
@Getter @Setter
public class Script {

    private String path;

    public Script(String path) {
        this.path = path;
        this.startScript();
    }

    private void startScript() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            Base.setup(engine);
            addCategories(engine);
            engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\Nick\\VSCode\\test\\main.js"), StandardCharsets.UTF_8));

            ScriptModule module = null;

            if(!Base.hasVariable(engine, "name") || !Base.hasVariable(engine, "category")) {
                System.err.println("Missing required variables!");
                System.exit(0);
                return;
            }

            if(Base.hasVariable(engine, "description")) module = new ScriptModule(Base.getString(engine, "name"), Base.getString(engine, "description"), (Category) Base.getVariable(engine, "category"));
            if(module == null) module = new ScriptModule(Base.getString(engine, "name"), (Category) Base.getVariable(engine, "category"));

            Slice.INSTANCE.getModuleManager().register(module);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCategories(ScriptEngine engine) {
        Base.putInEngine(engine, "Category", Category.values());
        Base.putInEngine(engine, "COMBAT", COMBAT);
        Base.putInEngine(engine, "MOVEMENT", MOVEMENT);
        Base.putInEngine(engine, "MISC", MISC);
        Base.putInEngine(engine, "RENDER", RENDER);
        Base.putInEngine(engine, "PLAYER", PLAYER);
        Base.putInEngine(engine, "WORLD", WORLD);
    }
}
