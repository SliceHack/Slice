package slice.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.Slice;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.Setting;
import slice.setting.settings.ModeValue;
import slice.util.LoggerUtil;
import slice.util.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the base of every module
 *
 * @author Nick
 */
@Getter @Setter
public abstract class Module {

    /* Module fields */
    protected Minecraft mc = Minecraft.getMinecraft();
    protected Timer timer = new Timer();

    /* info */
    private ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

    /* Module Data */
    private String name, description;
    private Category category;
    private int key;

    private boolean enabled;

    private List<Setting> settings = new ArrayList<>();

    public Module() {
        if(info == null) {
            throw new IllegalStateException("ModuleInfo is not present on module " + getClass().getName());
        }
        this.name = info.name();
        this.category = info.category();
        this.description = info.description();
        this.key = info.key();
    }

    public void toggle() {
        enabled = !enabled;
        if(enabled) onEnable();
        else onDisable();
        Slice.INSTANCE.getSaver().save();
    }

    public void onEnable() {}
    public void onDisable() {}
    public abstract void onEvent(Event event);

    /**
     * Gets a setting by name
     * @param name the name of the setting
     *             - if the setting is not found, null is returned
     * */
    public Setting getSetting(String name) {
        return settings.stream().filter(setting -> setting.getName().replace(" ", "").equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Gets the modules mode
     * */
    public ModeValue getMode() {
        return settings.stream().filter(setting -> (setting instanceof ModeValue && setting.getName().equalsIgnoreCase("mode"))).map(setting -> (ModeValue) setting).findFirst().orElse(null);
    }

    public void onUpdate(EventUpdate event) {

    }


}
