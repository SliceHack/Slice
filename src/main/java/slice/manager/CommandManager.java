package slice.manager;

import slice.command.Command;
import slice.command.commands.CommandSetting;
import slice.event.events.EventChat;
import slice.module.Module;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<Command> commands = new ArrayList<>();

    public CommandManager() {
        register(new CommandSetting());
    }

    public void register(Command command) {
        commands.add(command);
    }

    /**
     * Used to handle commands
     * */
    public void handleChat(EventChat event) {
        String message = event.getMessage();
        String command = message.split(" ")[0];
        String[] args = message.split(" ");
        args = removeFirstArgument(args);
        String[] finalArgs = args;

        commands.forEach(command1 -> {
            if(command1.getName().equalsIgnoreCase(command)) {
                command1.handle(command, finalArgs);
            }
            for(String alias : command1.getAliases()) {
                if(alias.equalsIgnoreCase(command)) {
                    command1.handle(command, finalArgs);
                }
            }
        });
    }

    /**
     * Used to remove the first argument from the array
     * @param args the array of arguments to remove the first argument from
     * */
    private String[] removeFirstArgument(String[] args) {
        String[] newArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        return newArgs;
    }
}
