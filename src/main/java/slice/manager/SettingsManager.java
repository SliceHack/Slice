package slice.manager;

import lombok.Getter;
import lombok.Setter;
import slice.setting.Setting;
import slice.util.LoggerUtil;

import java.lang.reflect.Field;

@Getter @Setter
public class SettingsManager {

    public SettingsManager(ModuleManager moduleManager) {
        moduleManager.getModules().forEach((module) -> {
            Field[] fields = module.getClass().getDeclaredFields();

            for(Field field : fields) {
                if(field.getType().getSuperclass().equals(Setting.class)) {
                    try {
                        field.setAccessible(true);
                        Setting setting = (Setting) field.get(module);
                        module.getSettings().add(setting);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
