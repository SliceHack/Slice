package slice.script;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import slice.Slice;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.module.ScriptModule;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    private List<Setting> settings = new ArrayList<>();

    private ScriptEngine engine;
    private Module module;

    private BufferedReader reader;

    public Script(String path, ModuleManager moduleManager, FontManager fontManager) {
        this.path = path;
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;
        this.startScript();
    }

    public void reloadScript() {
        this.startScript();
    }

    private void stopScript() {
        Bindings bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        Set<String> allAttributes = bind.keySet();
        for (String attr : allAttributes) {
            bind.remove(attr);
        }
        moduleManager.unregister(module);
    }


    private void startScript() {
        try {
            String[] args = new String[] { "--language=javascript", "--language=es6" };
            engine = new NashornScriptEngineFactory().getScriptEngine(args);

            Base.setup(engine);
            Base.putInEngine(engine, "Category", StaticClass.forClass(NumberValue.Type.class));
            Base.putInEngine(engine,"Type", StaticClass.forClass(NumberValue.Type.class));
            Base.putInEngine(engine, "script", this);
            engine.eval("function require(url) {script.require(url);};");

            if(path != null) reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);

            engine.eval(reader);
            if(!Base.hasVariable(engine, "name") || !Base.hasVariable(engine, "category")) {
                System.err.println("Missing required variables");
                return;
            }

            String name = (String)Base.getVariable(engine, "name");
            Category category = (Category)Base.getVariable(engine, "category");
            String description = Base.hasVariable(engine,"description") ? (String)Base.getVariable(engine, "description") : "No description provided.";

            module = new ScriptModule(name, description, category, engine, fontManager);
            if(moduleManager.getModule(name) != null && moduleManager.getModule(name) instanceof ScriptModule) { moduleManager.unregister(moduleManager.getModule(name)); }
            else if(moduleManager.getModule(name) != null) { System.err.println("A module with the name " + name + " already exists."); return; }

            moduleManager.register(module);
            module.setSettings(settings);
            Base.putInEngine(engine, "module", module);
            Base.callFunction(engine, "onLoad");
        } catch (Exception e) {
            LoggerUtil.addTerminalMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void require(String url) {
        boolean isURL = url.startsWith("http");
        if(isURL) {
            try {
                URL urlE = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlE.openStream()));
                engine.eval(reader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            File parent = new File(path).getParentFile();
            BufferedReader reader = Files.newBufferedReader(Paths.get(parent.getPath() + url.substring(1)), StandardCharsets.UTF_8);
            engine.eval(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BooleanValue registerBoolean(String name, boolean value) {
        BooleanValue setting = new BooleanValue(name, value);
        getSettings().add(setting);
        return setting;
    }

    public ModeValue registerMode(String name, String... modes) {
        if(modes.length == 0) return null;
        ModeValue mode = new ModeValue(name, modes[0], modes);
        settings.add(mode);
        return mode;
    }

    public NumberValue registerNumber(String name, double min, double max, double value, NumberValue.Type type) {
        NumberValue setting = new NumberValue(name, value, min, max, type);
        settings.add(setting);
        return setting;
    }

    public BlockPos blockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public EntityLivingBase getTarget() {
        return Slice.INSTANCE.target;
    }
}
