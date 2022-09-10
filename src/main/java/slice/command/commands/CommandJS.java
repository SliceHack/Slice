package slice.command.commands;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.script.lang.Base;
import slice.script.lang.logger.Chat;
import slice.util.*;

import javax.script.ScriptEngine;

@CommandInfo(name = "js", description = "Executes JavaScript code")
public class CommandJS extends Command {

    public final ScriptEngine engine;

    public CommandJS() {
        super();
        engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6");
        Base.setup(engine);
    }

    @Override
    public void init() {
        Base.putClassInEngine(engine, "Chat", Chat.class);
        Base.putClassInEngine(engine, "MoveUtil", MoveUtil.class);
        Base.putClassInEngine(engine, "KeyUtil", KeyUtil.class);
        Base.putClassInEngine(engine, "RenderUtil", RenderUtil.class);
        Base.putClassInEngine(engine, "RotationUtil", RotationUtil.class);
        Base.putClassInEngine(engine, "LoggerUtil", LoggerUtil.class);
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
