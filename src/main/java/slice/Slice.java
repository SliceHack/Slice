package slice;

import lombok.Getter;
import slice.event.Event;
import slice.event.events.EventKey;
import slice.font.FontManager;
import slice.manager.ModuleManager;
import slice.module.Module;

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
    private final FontManager fontManager;

    /**
     * TODO:
         * - Fix Button Scaling in MainMenu
         *
     * */
    Slice() {
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
    }

    public void onEvent(Event event) {
        if(event instanceof EventKey) {
            EventKey e = (EventKey) event;
            moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle); // key event
        }
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onEvent(event)); // Module events
    }
}