package slice.script;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import slice.Slice;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.modules.combat.Aura;
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
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.Buffer;
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

    private ScriptEngine engine;
    private Module module;

    private BufferedReader reader;

    public Script(String path, ModuleManager moduleManager, FontManager fontManager) {
        this.path = path;
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;
        this.startScript();
    }

    public Script(BufferedReader reader, ModuleManager moduleManager, FontManager fontManager) {
        this.reader = reader;
        this.moduleManager = moduleManager;
        this.fontManager = fontManager;
        this.startScript();
    }

    public void reloadScript() {
        this.stopScript();
        Base.setup(engine);
        this.addCategories(engine);
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
            addCategories(engine);
            Base.putInEngine(engine, "DOUBLE", NumberValue.Type.DOUBLE);
            Base.putInEngine(engine, "FLOAT", NumberValue.Type.FLOAT);
            Base.putInEngine(engine, "INTEGER", NumberValue.Type.INTEGER);
            Base.putInEngine(engine, "LONG", NumberValue.Type.LONG);

            if(reader == null && path != null) reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
            if(reader == null) { System.err.println("Reader is null"); return; }

            engine.eval(reader);

            if(!Base.hasVariable(engine, "name") || !Base.hasVariable(engine, "category")) {
                System.err.println("Missing required variables");
                return;
            }

            String name = (String)Base.getVariable(engine, "name");
            Category category = (Category)Base.getVariable(engine, "category");
            String description = Base.hasVariable(engine,"description") ? (String)Base.getVariable(engine, "description") : "No description provided.";

            module = new ScriptModule(name, description, category, engine, fontManager);
            module.setSettings(settings);
            moduleManager.register(module);
            Base.putInEngine(engine, "module", module);
            Base.callFunction(engine, "onLoad");
        } catch (Exception e) {
            LoggerUtil.addTerminalMessage(e.getMessage());
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

    public BlockPos blockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public EntityLivingBase getTarget() {
        return Slice.INSTANCE.target;
    }
}
