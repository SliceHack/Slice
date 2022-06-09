package slice.command.commands;

import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.module.Module;

@CommandInfo(name = "toggle", description = "Toggles a module", aliases = {"t"})
public class CommandToggle extends Command {

    @Override
    public boolean handle(String name, String[] args) {
        if(args.length < 1) {
            addMessage("Usage: toggle <module>");
            return false;
        }
        Module module = Slice.INSTANCE.getModuleManager().getModule(args[0]);
        if(module == null) {
            addMessage("Module not found");
            return false;
        }
        module.setEnabled(!module.isEnabled());
        addMessage("Module " + module.getName() + " is now " + (module.isEnabled() ? "enabled" : "disabled"));
        return false;
    }
}
