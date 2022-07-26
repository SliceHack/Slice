package slice.script;

import lombok.Getter;
import lombok.Setter;
import slice.font.FontManager;
import slice.manager.ModuleManager;
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
    private ModuleManager moduleManager;
    private FontManager fontManager;

    public Script(String path, ModuleManager moduleManager, FontManager fontManager) {
        this.path = path;
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;
        this.startScript();
    }

    private void startScript() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            Base.setup(engine);
            addCategories(engine);

            engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\Nick\\VSCode\\test\\main.js"), StandardCharsets.UTF_8));

            if(!Base.hasVariable(engine, "name") || !Base.hasVariable(engine, "category")) {
                System.err.println("Missing required variables");
                return;
            }

            if(!(Base.getVariable(engine, "category") instanceof Category)) {
                System.out.println("Category is invalid type");
                return;
            }

            String name = (String)Base.getVariable(engine, "name");
            Category category = (Category)Base.getVariable(engine, "category");
            String description = Base.hasVariable(engine,"description") ? (String)Base.getVariable(engine, "description") : "No description provided.";

            ScriptModule module = new ScriptModule(name, description, category, engine, fontManager);
            moduleManager.register(module);
            Base.callFunction(engine, "onLoad");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCategories(ScriptEngine engine) {
        Base.putInEngine(engine, "COMBAT", COMBAT);
        Base.putInEngine(engine, "MOVEMENT", MOVEMENT);
        Base.putInEngine(engine, "MISC", MISC);
        Base.putInEngine(engine, "RENDER", RENDER);
        Base.putInEngine(engine, "PLAYER", PLAYER);
        Base.putInEngine(engine, "WORLD", WORLD);
    }
}
