package slice.script.manager;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import slice.Slice;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.script.Script;
import slice.script.module.ScriptModule;
import slice.util.LoggerUtil;

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

    public void load() {
        for(File file : Objects.requireNonNull(scriptDataDir.listFiles())) {
            if(file.getName().endsWith(".js")) {
                scripts.add(new Script(file.getAbsolutePath(), moduleManager, fontManager));
            }
        }
    }

}
