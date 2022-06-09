package slice.command.commands;

import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.module.Module;

@CommandInfo(name = "bind", description = "Binds a command to a key")
public class CommandBind extends Command {

    @Override
    public boolean handle(String name, String[] args) {
        if(args.length == 0) {
            addMessage("&cUsage: bind <key> <command>");
            return true;
        }
        if(args.length == 1) {
            addMessage("&cUsage: bind <command> <key>");
            return true;
        }

        Module module = Slice.INSTANCE.getModuleManager().getModule(args[0]);
        if(module == null) {
            addMessage("&cModule not found");
            return true;
        }
        int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        args[1] = args[1].toUpperCase();
        addMessage("&aBound " + module.getName() + " to " + args[1]);
        return true;
    }
}
