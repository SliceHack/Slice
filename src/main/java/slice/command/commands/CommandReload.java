package slice.command.commands;

import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.script.Script;

@CommandInfo(name = "reload", description = "Reloads a script", aliases = {"reloadscript"})
public class CommandReload extends Command {

    @Override
    public boolean handle(String name, String[] args) {
        if(args.length == 0) {
            addMessage("&cUsage&7: reload <script>");
            return false;
        }
        String path = args[0];

        Script script = Slice.INSTANCE.getScriptManager().getScript(path);
        if(script == null) {
            addMessage("&cScript not found");
            return false;
        }
        script.reloadScript();
        addMessage("&aScript reloaded");
        return false;
    }
}