package slice.script;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.Getter;
import lombok.Setter;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.module.ScriptModule;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public Script(String path, ModuleManager moduleManager, FontManager fontManager) {
        this.path = path;
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;
        this.startScript();
    }

    private void startScript() {
        try {
            String[] args = new String[] { "--language=javascript", "--language=es6" };
            ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine(args);

            Base.setup(engine);
            addCategories(engine);
            engine.put("DOUBLE", NumberValue.Type.DOUBLE);
            engine.put("FLOAT", NumberValue.Type.FLOAT);
            engine.put("INTEGER", NumberValue.Type.INTEGER);
            engine.put("LONG", NumberValue.Type.LONG);
            engine.put("script", this);

            engine.eval(Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8));

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
            module.setSettings(settings);
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

    public BooleanValue registerSettingBoolean(String name, boolean value) {
        BooleanValue setting = new BooleanValue(name, value);
        getSettings().add(setting);
        return setting;
    }

    public ModeValue registerSettingMode(String name, String... modes) {
        if(modes.length == 0) return null;
        ModeValue mode = new ModeValue(name, modes[0], modes);
        settings.add(mode);
        return mode;
    }

    public NumberValue registerSettingNumber(String name, double min, double max, double value, NumberValue.Type type) {
        NumberValue setting = new NumberValue(name, value, min, max, type);
        settings.add(setting);
        return setting;
    }
}
