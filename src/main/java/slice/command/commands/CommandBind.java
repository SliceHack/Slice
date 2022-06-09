package slice.command.commands;

import slice.command.Command;
import slice.command.data.CommandInfo;

@CommandInfo(name = "bind", description = "Binds a command to a key")
public class CommandBind extends Command {

    @Override
    public boolean handle(String name, String[] args) {
        if(args.length == 0) {
            addMessage("Usage: bind <key> <command>");
        }
        return true;
    }
}
