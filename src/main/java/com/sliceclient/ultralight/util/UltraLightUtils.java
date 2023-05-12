package com.sliceclient.ultralight.util;

import com.labymedia.ultralight.input.UltralightMouseEventButton;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UltraLightUtils {

    public UltralightMouseEventButton getButtonByButtonID(int key) {
        switch (key) {
            case 0: return UltralightMouseEventButton.LEFT;
            case 1: return UltralightMouseEventButton.RIGHT;
            case 2: return UltralightMouseEventButton.MIDDLE;
            default: return null;
        }
    }
}
