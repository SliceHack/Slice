package com.sliceclient.ultralight;

import lombok.Getter;
import slice.Slice;
import slice.event.manager.EventManager;

public class UltraLightEngine {
    @Getter
    private final Slice slice = Slice.INSTANCE;

    @Getter
    private final EventManager eventManager = slice.getEventManager();


    public UltraLightEngine() {

       eventManager.register(this);
    }

    public void extractNativeLibraries() {
    }
}
