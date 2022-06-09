package slice.command.commands;

import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

@CommandInfo(name = "setting", description = "Change settings")
public class CommandSetting extends Command {

    public CommandSetting(ModuleManager moduleManager) {
        moduleManager.getModules().forEach(module -> addAlias(module.getName()));
    }

    @Override
    public boolean handle(String name, String[] args) {
        if(name.equalsIgnoreCase("setting")) {
            addMessage("&cUsage: .<module> <setting> <value>");
            return false;
        }
        Module module = Slice.INSTANCE.getModuleManager().getModule(name);
        if(module == null) {
            addMessage("&cModule not found");
            return false;
        }
        if(args.length < 2) {
            addMessage("&cUsage: ." + name + " <setting> <value>");
            return false;
        }
        Setting setting = module.getSetting(args[0]);
        if(setting == null) {
            addMessage("&cSetting not found");
            return false;
        }

        if(setting instanceof ModeValue) {
            ModeValue mode = (ModeValue) setting;
            for(String s : mode.getValues()) {
                if(s.equalsIgnoreCase(args[1])) {
                    mode.setValue(s);
                    addMessage("&a" + args[0] + " set to " + s);
                    return true;
                }
            }
            addMessage("&cValue not found");
        }
        if(setting instanceof BooleanValue) {
            BooleanValue booleanValue = (BooleanValue) setting;
            boolean value;
            if(args[1] == null) value = !booleanValue.getValue();
            else value = Boolean.parseBoolean(args[1]);
            booleanValue.setValue(value);
            addMessage("&a" + args[0] + " set to " + value);
        }
        if(setting instanceof NumberValue) {
            NumberValue numberValue = (NumberValue) setting;
            double value;
            if(args[1] == null) value = numberValue.getValue().doubleValue();
            else value = Double.parseDouble(args[1]);

            numberValue.setValue(value);
            addMessage("&a" + args[0] + " set to " + value);
        }
        return false;
    }
}
