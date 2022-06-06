package slice;

import lombok.Getter;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventKey;
import slice.util.LoggerUtil;

@Getter
public enum Slice {
    INSTANCE;

    public static String NAME = "Slice", VERSION = "1.0";

    Slice(){
    }

    public void onEvent(Event event) {
        if(event instanceof EventKey) {
            EventKey key = (EventKey) event;
            LoggerUtil.addMessage(Keyboard.getKeyName(key.getKey()) + " was typed");
        }
    }
}