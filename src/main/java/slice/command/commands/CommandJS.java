package slice.command.commands;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.script.lang.Base;
import slice.script.module.util.ScriptLoggerUtil;
import slice.script.module.util.ScriptMoveUtil;
import slice.script.module.util.ScriptRotationUtil;
import slice.util.LoggerUtil;

import javax.script.ScriptEngine;

@CommandInfo(name = "js", description = "Executes JavaScript code")
public class CommandJS extends Command {

    private final ScriptEngine engine;

    public CommandJS() {
        super();
        engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6");
        Base.setup(engine);
    }

    @Override
    public void init() {
        Base.putInEngine(engine, "LoggerUtil", ScriptLoggerUtil.INSTANCE);
        Base.putInEngine(engine, "MoveUtil", ScriptMoveUtil.INSTANCE);
        Base.putInEngine(engine, "RotationUtil", ScriptRotationUtil.INSTANCE);
        Base.putInEngine(engine, "ModuleManager", Slice.INSTANCE.getModuleManager());
    }

    @Override
    public boolean handle(String name, String[] args) {
        String message = String.join(" ", args);

        if(message.isEmpty()) {
            addMessage("Please enter some JavaScript code to execute");
            return true;
        }

        try {
            engine.eval(message);
        } catch (Exception e) {
            addMessage("&c" + e.getMessage());
        }
        return true;
    }
}
