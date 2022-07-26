package slice.script.module;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;

import javax.script.ScriptEngine;

public class ScriptModule extends Module {

    private final ScriptEngine engine;

    public ScriptModule(String name, String description, Category category, ScriptEngine engine) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.engine = engine;
        engine.put("chat", Chat.INSTANCE);
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
