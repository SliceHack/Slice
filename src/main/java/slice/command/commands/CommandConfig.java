package slice.command.commands;

import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.file.ConfigSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@CommandInfo(name = "config", description = "Config")
public class CommandConfig extends Command {

    public boolean handle(String name, String[] args) {
        if(args.length < 2) {
            addMessage("&cUsage&7: .config <save/load> <config>");
            return true;
        }
        String action = args[0];
        String config = args[1];

        File configFile = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\configs\\" + config + ".json");

        if(action.equalsIgnoreCase("load") && !configFile.exists()) {
            addMessage("&cConfig &7" + config + " &cdoes not exist!");
            return true;
        }

        ConfigSaver configS = new ConfigSaver(config, Slice.INSTANCE.getModuleManager());

        if(action.equalsIgnoreCase("load")) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String configString = sb.toString();
                JSONObject configObject = new JSONObject(configString);

                if (Double.parseDouble(configObject.getString("build")) < Double.parseDouble(Slice.VERSION)) {
                    addMessage("&cThis config was made in a older version of Slice and may not work properly!");
                }
            } catch (Exception ignored) {}
        }

        if(action.equalsIgnoreCase("load")) {
            configS.load();
            addMessage("Loaded " + config);
        }

        if(action.equalsIgnoreCase("save")) {
            if(!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();

            configS.save();
            addMessage("Saved " + config);
        }

        return false;
    }
}
