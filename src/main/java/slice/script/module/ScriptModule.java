package slice.script.module;

import slice.Slice;
import slice.font.FontManager;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.Script;
import slice.script.lang.logger.Chat;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.*;

import javax.script.ScriptEngine;

import static slice.script.lang.Base.*;

@SuppressWarnings("unused")
public class ScriptModule extends Module {

    private final ScriptEngine engine;
    private final Script script;

    public ScriptModule(Script script, String name, String description, Category category, ScriptEngine engine, FontManager fontManager) {
        this.script = script;
        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
    }

    @Override
    public void init() {
        putInEngine(engine,"FontManager", Slice.INSTANCE.getFontManager());
        putInEngine(engine, "module", this);
        putInEngine(engine, "ModuleManager", Slice.INSTANCE.getModuleManager());
        putInEngine(engine, "CommandManager", Slice.INSTANCE.getCommandManager());
        putInEngine(engine, "player", mc.thePlayer);
        putInEngine(engine,"timer", timer);
        script.call("init");
        super.init();
    }

    @Override
    public void onEnable() {
        script.call("enable");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        script.call("disable");
        super.onDisable();
    }

    public BooleanValue registerSettingBoolean(String name, boolean value) {
        BooleanValue setting = new BooleanValue(name, value);
        getSettings().add(setting);
        return setting;
    }

    public ModeValue registerSettingMode(String name, String... modes) {
        if(modes.length == 0) return null;
        ModeValue mode = new ModeValue(name, modes[0], modes);
        getSettings().add(mode);
        return mode;
    }

    public NumberValue registerSettingNumber(String name, double min, double max, double value, NumberValue.Type type) {
        NumberValue setting = new NumberValue(name, min, max, value, type);
        getSettings().add(setting);
        return setting;
    }
}
