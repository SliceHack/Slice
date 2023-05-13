package com.sliceclient.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import com.labymedia.ultralight.os.OperatingSystem;
import com.sliceclient.ultralight.view.View;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UltraLightEngine {

    @Getter
    private static UltraLightEngine instance;

    @Getter
    private final UltralightRenderer renderer;

    @Getter
    private final UltralightPlatform platform;

    @Getter
    private final UltraLightEvents ultraLightEvents;

    @Getter
    private final List<View> views = new ArrayList<>();

    public UltraLightEngine() {
        instance = this;
        try {
            ResourceManager.downloadUltralight();

            String[] libs = new String[] {
                    "glib-2.0-0",
                    "gobject-2.0-0",
                    "gmodule-2.0-0",
                    "gio-2.0-0",
                    "gstreamer-full-1.0",
                    "gthread-2.0-0"
            };

            Path natives = ResourceManager.binDir.toPath();
            OperatingSystem os = OperatingSystem.get();
            for(String lib : libs) {
                System.load(natives.resolve(os.mapLibraryName(lib)).toAbsolutePath().toString());
            }

            UltralightJava.load(natives);
            UltralightGPUDriverNativeUtil.load(natives);
        } catch (URISyntaxException | UltralightLoadException | IOException e) {
            throw new RuntimeException(e);
        }


        platform = UltralightPlatform.instance();
        platform.setConfig(
                new UltralightConfig()
                        .fontHinting(FontHinting.SMOOTH)
        );
        platform.usePlatformFontLoader();
        platform.usePlatformFileSystem(ResourceManager.ultraLightDir.getAbsolutePath());
        renderer = UltralightRenderer.create();

        ultraLightEvents = new UltraLightEvents(this);
    }

    public void registerView(View view) {
        views.add(view);
    }

    public void unregisterView(View view) {
        views.remove(view);
    }
}
