package slice.file;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import slice.Slice;
import slice.cef.RequestHandler;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

import java.awt.*;
import java.io.*;

@Getter @Setter
@SuppressWarnings("all")
public class Saver {

    private File modules = new File(Minecraft.getMinecraft().mcDataDir, "Slice\\client.json");

    private ModuleManager moduleManager;

    public Saver(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        this.load();
    }

    public void load() {
        if(!modules.exists()) {
            modules.getParentFile().mkdirs();
            return;
        }

        try {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(modules));
            } catch (FileNotFoundException e) {
                return;
            }

            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            JSONObject json = new JSONObject(builder.toString());
            for(Module module : moduleManager.getModules()) {
                JSONObject moduleJson;
                try {
                    moduleJson = json.getJSONObject(module.getName());
                } catch (Exception e) {
                    return;
                }
                module.setEnabled(moduleJson.getBoolean("enabled"));
                module.setKey(moduleJson.getInt("key"));

                JSONObject settingsJson = moduleJson.getJSONObject("settings");
                for(Setting key : module.getSettings()) {
                    if(!settingsJson.has(key.getName()))
                        return;

                    if(key instanceof BooleanValue) {
                        ((BooleanValue) key).setValue(settingsJson.getBoolean(key.getName()));
                    }
                    if(key instanceof ModeValue) {
                        ((ModeValue) key).setValue(settingsJson.getString(key.getName()));
                    }
                    if(key instanceof NumberValue) {
                        NumberValue value1 = (NumberValue) module.getSetting(key.getName());
                        Number value;
                        try {
                            switch (value1.getType()) {
                                case DOUBLE:
                                    value = settingsJson.getDouble(key.getName());
                                    break;
                                case FLOAT:
                                    value = settingsJson.getFloat(key.getName());
                                    break;
                                case LONG:
                                    value = settingsJson.getLong(key.getName());
                                    break;
                                default:
                                    value = settingsJson.getInt(key.getName());
                                    break;
                            }
                            value1.setValue(value);
                        } catch (Exception ignored){}
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JSONObject json = new JSONObject();
        for(Module module : moduleManager.getModules()) {
            JSONObject moduleJson = new JSONObject();
            moduleJson.put("enabled", module.isEnabled());
            moduleJson.put("key", module.getKey());

            JSONObject settingsJson = new JSONObject();
            for(Setting key : module.getSettings()) {
                if(key instanceof BooleanValue) {
                    settingsJson.put(key.getName(), ((BooleanValue) key).getValue());
                }
                if(key instanceof ModeValue) {
                    settingsJson.put(key.getName(), ((ModeValue) key).getValue());
                }
                if(key instanceof NumberValue) {
                    settingsJson.put(key.getName(), ((NumberValue) key).getValue());
                }
            }
            moduleJson.put("settings", settingsJson);
            json.put(module.getName(), moduleJson);

            json.put("build", Slice.VERSION);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(modules));
                writer.write(json.toString());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
