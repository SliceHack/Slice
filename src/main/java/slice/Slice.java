package slice;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventKey;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.manager.SettingsManager;
import slice.module.Module;
import slice.util.LoggerUtil;
import slice.util.ResourceUtil;
import slice.util.account.LoginUtil;

/**
* Main Class for the Client
*
* @author Nick
*/
@Getter
public enum Slice {
    INSTANCE;

    public static String NAME = "Slice", VERSION = "1.0";

    /* managers */
    private final ModuleManager moduleManager;
    private final SettingsManager settingsManager;
    private final FontManager fontManager;

    Slice() {
        moduleManager = new ModuleManager();
        settingsManager = new SettingsManager(moduleManager);
        fontManager = new FontManager();
    }

    /**
     * Where all events are handled
     *
     * @pamra event - the event to be handled
     * */
    public void onEvent(Event event) {
        if(event instanceof EventKey) {
            EventKey e = (EventKey) event;
            moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
        }
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onEvent(event)); // Module events
    }


}