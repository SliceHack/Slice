package slice.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import slice.event.Event;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

/**
 * Class of the base of every module
 *
 * @author Nick
 */
@Getter @Setter
public abstract class Module {

    /* Module fields */
    protected Minecraft mc = Minecraft.getMinecraft();

    /* info */
    private ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

    /* Module Data */
    private String name, description;
    private Category category;
    private int key;

    private boolean enabled;

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
        if(enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public abstract void onEvent(Event event);


}
