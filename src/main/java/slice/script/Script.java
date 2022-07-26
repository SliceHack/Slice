package slice.script;
import lombok.Getter;
import lombok.Setter;
import slice.script.lang.Base;
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
            engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\djlev\\OneDrive\\Documents\\SliceScript\\index.js"), StandardCharsets.UTF_8));

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
