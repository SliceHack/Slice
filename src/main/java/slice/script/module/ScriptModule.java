package slice.script.module;

import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.*;
import slice.font.FontManager;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.Script;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.*;

import javax.script.ScriptEngine;

@SuppressWarnings("unused")
public class ScriptModule extends Module {

    private final ScriptEngine engine;
    private final Script script;

    public ScriptModule(Script script, String name, String description, Category category, ScriptEngine engine, FontManager fontManager) {
        Base.putClassInEngine(engine, "Chat", Chat.class);
        Base.putClassInEngine(engine, "MoveUtil", MoveUtil.class);
        Base.putClassInEngine(engine, "KeyUtil", KeyUtil.class);
        Base.putClassInEngine(engine, "RenderUtil", RenderUtil.class);
        Base.putClassInEngine(engine, "RotationUtil", RotationUtil.class);
        Base.putClassInEngine(engine, "LoggerUtil", LoggerUtil.class);
        Base.putClassInEngine(engine, "PacketUtil", PacketUtil.class);
        Base.putInEngine(engine,"timer", timer);

        this.script = script;
        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
    }

    @Override
    public void onUpdateNoToggle(EventUpdate event) {
        engine.put("player", mc.thePlayer);
        engine.put("FontManager", Slice.INSTANCE.getFontManager());
        engine.put("timer", timer);
    }

    @Override
    public void init() {
        script.call("init");
        Base.putInEngine(engine, "module", this);
        Base.putInEngine(engine, "ModuleManager", Slice.INSTANCE.getModuleManager());
        Base.putInEngine(engine, "CommandManager", Slice.INSTANCE.getCommandManager());
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

    @EventInfo
    public void onUpdate(EventUpdate e) {
        Base.callFunction(engine, "onUpdate", e);
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
