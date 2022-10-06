package slice.script.manager;

import com.sliceclient.script.ScriptLoader;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.script.Script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The ScriptManager class
 *
 * @author Nick
 */
@Getter
public class ScriptManager {

    /** dir */
    public File scriptDataDir = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\Scripts\\");

    private final List<Script> scripts = new ArrayList<>();

    /** managers */
    private final ModuleManager moduleManager;
    private final FontManager fontManager;

    @SuppressWarnings("all")
    public ScriptManager(ModuleManager moduleManager, FontManager fontManager) {
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;

        if(!scriptDataDir.exists()) scriptDataDir.mkdirs();

        this.load();
    }

    public Script getScript(String path) {
        for(Script script : scripts) {
            String lastPath = script.getPath().substring(script.getPath().lastIndexOf("\\") + 1);

            if(!path.endsWith(".js")) path += ".js";

            if(lastPath.equalsIgnoreCase(path)) {
                return script;
            }
        }
        return null;
    }

    public void load() {
        for(File file : Objects.requireNonNull(scriptDataDir.listFiles())) {
            if(file.getName().endsWith(".js")) {
                scripts.add(new Script(file.getAbsolutePath(), moduleManager, fontManager));
            }
            if(file.getName().endsWith(".jar")) {
                new ScriptLoader(file);
            }
        }
    }

    public void callEvent(String name, Object... args) {
        scripts.forEach(script -> script.call(name, args));
    }

}
