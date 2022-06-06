package slice;

import lombok.Getter;
import slice.event.Event;
import slice.manager.ModuleManager;

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

    }
}