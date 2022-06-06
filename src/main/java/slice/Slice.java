package slice;

import lombok.Getter;
import slice.event.Event;

@Getter
public enum Slice {
    INSTANCE;

    public static String NAME = "Slice", VERSION = "1.0";

    Slice(){}

    public void onEvent(Event event) {}
}