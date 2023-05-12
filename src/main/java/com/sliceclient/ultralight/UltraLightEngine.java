package com.sliceclient.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import com.labymedia.ultralight.os.OperatingSystem;
import com.sliceclient.api.event.data.EventInfo;
import com.sliceclient.ultralight.util.ULResourceManager;
import com.sliceclient.ultralight.view.DynamicGuiView;
import com.sliceclient.ultralight.view.View;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import slice.event.events.EventKey;
import slice.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UltraLightEngine {

    @Getter
    public static UltraLightEngine instance;


    @Getter
    private final List<View> views = new ArrayList<>();

    @Getter
    private UltralightRenderer renderer;

    @Getter
    private UltralightPlatform platform;

    @Getter
    private final UltraLightEvents ultraLightEvents;

    public UltraLightEngine() {
        instance = this;

        try {
            ULResourceManager.loadUltralight();

            Path natives = ULResourceManager.binDir.toPath();
            OperatingSystem os = OperatingSystem.get();

            for(String lib : new String[] {
                    "glib-2.0-0",
                    "gobject-2.0-0",
                    "gmodule-2.0-0",
                    "gio-2.0-0",
                    "gstreamer-full-1.0",
            }) {
                System.load(natives.resolve(os.mapLibraryName(lib)).toAbsolutePath().toString());
                System.out.println("Loaded " + lib);
            }

            UltralightJava.load(natives.toFile().toPath());
            UltralightGPUDriverNativeUtil.load(natives.toFile().toPath());
            System.out.println("Ultralight loaded");
        } catch (URISyntaxException | UltralightLoadException | IOException e) {
            throw new RuntimeException(e);
        }

        platform = UltralightPlatform.instance();
        platform.setConfig(
                new UltralightConfig()
                        .fontHinting(FontHinting.NORMAL)
                        .resourcePath(ULResourceManager.resourceDir.getAbsolutePath())
        );

        renderer = UltralightRenderer.create();
        renderer.logMemoryUsage();
        this.ultraLightEvents = new UltraLightEvents(this);
    }

    public void registerView(View view) {
        views.add(view);
    }

    public void unregisterView(View view) {
//        private final static String SDK_URL = "https://ultralight-sdk.sfo2.cdn.digitaloceanspaces.com/ultralight-sdk-latest-";

        views.remove(view);
        view.close();
    }

    @EventInfo
    public void onKey(EventKey e) {
        int key = e.getKey();

        LoggerUtil.addMessage("Key: " + key);
        if(key == Keyboard.KEY_Z) {
            Minecraft.getMinecraft().displayGuiScreen(new DynamicGuiView(
                    new Page(
                            "https://google.com"
                    )
            ));
        }
    }

}
