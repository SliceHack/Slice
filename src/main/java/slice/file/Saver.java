package slice.file;

import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import slice.manager.ModuleManager;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@SuppressWarnings("all")
public class Saver {

    public File main, modules;
    private ModuleManager moduleManager;

    public Saver(ModuleManager moduleManager) {
        main = new File(Minecraft.getMinecraft().mcDataDir, "Slice");
        modules = new File(main, "modules.json");

        if(modules.exists()) {
            modules.getParentFile().mkdirs();
            return;
        }

        this.moduleManager = moduleManager;
        load();
    }

    public void save() {
        try {
            JSONObject json = new JSONObject();

            JSONObject modules = new JSONObject();
            for(Module module : moduleManager.getModules()) {
                JSONObject moduleJson = new JSONObject();
                moduleJson.put("enabled", module.isEnabled());
                moduleJson.put("key", module.getKey());
                modules.put(module.getName(), moduleJson);

                JSONObject settings = new JSONObject();
                for(Setting setting : module.getSettings()) {
                    JSONObject settingJson = new JSONObject();
                    if(setting instanceof ModeValue) {
                        ModeValue mv = (ModeValue) setting;
                        settingJson.put("value", mv.getValue());
                    }
                    if(setting instanceof BooleanValue) {
                        BooleanValue bv = (BooleanValue) setting;
                        settingJson.put("value", bv.getValue());
                    }
                    if(setting instanceof NumberValue) {
                        NumberValue nv = (NumberValue) setting;
                        settingJson.put("value", nv.getValue());
                    }
                }
            }
            json.put("modules", modules);

            FileWriter writer = new FileWriter(this.modules);
            writer.write(json.toString());
            writer.flush();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        JSONObject jsonObject = new JSONObject(readFile());

        JSONObject modules = jsonObject.getJSONObject("modules");
        for(Module module : moduleManager.getModules()) {

            // module data
            JSONObject moduleObject;
            try {
                moduleObject = modules.getJSONObject(module.getName());
                module.setEnabled(moduleObject.getBoolean("enabled"));
                module.setKey(moduleObject.getInt("key"));
            } catch (Exception ignored){
                return;
            }

            // module settings
            JSONObject settings = moduleObject.getJSONObject("settings");
            for(Setting s : module.getSettings()) {
                JSONObject setting = settings.getJSONObject(s.getName());

                try {
                    if (s instanceof ModeValue) {
                        ModeValue mv = (ModeValue) s;
                        mv.setValue(setting.getString("value"));
                    }

                    if(s instanceof BooleanValue) {
                        BooleanValue bv = (BooleanValue) s;
                        bv.setValue(setting.getBoolean("value"));
                    }

                    if(s instanceof NumberValue) {
                        NumberValue nv = (NumberValue) s;
                        switch (nv.getType()) {
                            case INTEGER:
                                nv.setValue(setting.getInt("value"));
                                break;
                            case DOUBLE:
                                nv.setValue(setting.getDouble("value"));
                                break;
                            case FLOAT:
                                nv.setValue(setting.getFloat("value"));
                                break;
                            case LONG:
                                nv.setValue(setting.getLong("value"));
                                break;
                        }
                    }
                } catch (Exception ignored){}

            }
        }
    }

    private String readFile() {
        StringBuilder sb = new StringBuilder();
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(modules);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();


    }
}
