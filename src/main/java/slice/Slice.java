package slice;

import lombok.Getter;
import slice.event.Event;
import slice.event.events.EventKey;
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

    Slice() {
        moduleManager = new ModuleManager();
    }

    public void onEvent(Event event) {
        if(event instanceof EventKey) {
            EventKey e = (EventKey) event;
            moduleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(module -> module.onEvent(event)); // key event
        }
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onEvent(event)); // Module events
    }
}