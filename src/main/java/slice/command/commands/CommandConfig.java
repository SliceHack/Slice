package slice.command.commands;

import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import slice.Slice;
import slice.command.Command;
import slice.command.data.CommandInfo;
import slice.file.ConfigSaver;
import slice.module.modules.render.HUD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@CommandInfo(name = "config", description = "Config")
public class CommandConfig extends Command {

    public boolean handle(String name, String[] args) {
        if(args.length < 1) {
            addMessage("&cUsage&7: .config <save/list/load> <config>");
            return true;
        }
        String action = args[0];


        if(action.equalsIgnoreCase("list")) {
            // loop through all the configs and print them out
            File folder = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\configs\\");
            File[] listOfFiles = folder.listFiles();

            StringBuilder sb = new StringBuilder();
            sb.append("&7Configs: ");
            if(listOfFiles != null) {
                int index = 0;
                for(File file : listOfFiles) {
                    String fileName = file.getName();
                    fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    if(index == (listOfFiles.length - 1)) {
                        sb.append("&c").append(fileName);
                    } else {
                        sb.append("&c").append(fileName).append(", ");
                    }
                }
            }
            addMessage(sb.toString());

            return true;
        }

        if(args.length < 2) {
            addMessage("&cUsage&7: .config <save/list/load> <config>");
            return true;
        }

        String config = args[1];

        File configFile = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\configs\\" + config + ".json");
        if(action.equalsIgnoreCase("load") && !configFile.exists()) {
            addMessage("&cConfig &7" + config + " &c" + "does not exist!");
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
            } catch (Exception e) {
                addMessage("&c" + e.getMessage());
                for(StackTraceElement element : e.getStackTrace()) {
                    addMessage(element.toString());
                }
            }
        }

        if(action.equalsIgnoreCase("load")) {
            Slice.INSTANCE.getModuleManager().getModules().forEach(module -> {
                if(module instanceof HUD)
                    return;

                module.setEnabled(false);
            });
            configS.load();
            addMessage("Loaded " + config);
        }

        if(action.equalsIgnoreCase("save")) {
            configS.save();
            addMessage("Saved " + config);
        }

        return false;
    }
}
