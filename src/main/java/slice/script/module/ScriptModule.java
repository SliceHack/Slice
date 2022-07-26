package slice.script.module;

import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;
import slice.script.lang.util.ScriptMoveUtil;

import javax.script.ScriptEngine;

public class ScriptModule extends Module {

    private final ScriptEngine engine;

    public ScriptModule(String name, String description, Category category, ScriptEngine engine) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
        engine.put("chat", Chat.INSTANCE);
        engine.put("MoveUtil", ScriptMoveUtil.INSTANCE);
        init();
    }

    @Override
    public void onUpdateNoToggle(EventUpdate event) {
        engine.put("player", mc.thePlayer);
    }

    @Override
    public void init() {
        Base.callFunction(engine, "init");
        super.init();
    }

    @Override
    public void onEnable() {
        Base.callFunction(engine, "onEnable");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Base.callFunction(engine, "onDisable");
        super.onDisable();
    }

}
