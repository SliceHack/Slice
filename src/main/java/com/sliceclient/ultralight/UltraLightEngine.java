package com.sliceclient.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import com.sliceclient.ultralight.support.ClipboardAdapter;
import com.sliceclient.ultralight.support.FileSystemAdapter;
import com.sliceclient.ultralight.view.View;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.util.FileUtil;
import slice.util.LoggerUtil;
import slice.util.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Made by CCBlueX (just converted to Java)
 */
@Getter @Setter
@SuppressWarnings("all")
public class UltraLightEngine {
    public static UltraLightEngine INSTANCE;

    private final Logger logger = LogManager.getLogger("Ultralight");
    private final String ULTRALIGHT_NATIVE_VERSION = "0.46";

    private UltralightPlatform platform;
    private UltralightRenderer renderer;

    private File ultralightPath = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Slice", "ultralight"),
            resourcePath = new File(ultralightPath, "resources"),
            pagesPath = new File(ultralightPath, "pages"),
            cachePath = new File(ultralightPath, "cache");


    int width = 0, height = 0, scaledWidth = 0, scaledHeight = 0, factor =1;
    private Timer gcTimer = new Timer();

    private List<View> views = new ArrayList<>();

    public UltraLightEngine() {
        INSTANCE = this;
        if(!pagesPath.exists()) pagesPath.mkdirs();
        if(!cachePath.exists()) cachePath.mkdirs();
    }

    public void init() {
        try {
            // download ultralight natives and resources from web
            checkResources();

            // then load it
            UltralightJava.load(resourcePath.toPath());

            platform = UltralightPlatform.instance();
            platform.setConfig(
                    new UltralightConfig()
                            .forceRepaint(false)
                            .resourcePath(resourcePath.getAbsolutePath().toString())
                            .cachePath(cachePath.getAbsolutePath().toString())
                            .fontHinting(FontHinting.SMOOTH)
            );
            platform.usePlatformFontLoader();
            platform.setFileSystem(new FileSystemAdapter());
            platform.setClipboard(new ClipboardAdapter());
            platform.setLogger((level, message) -> logger.info(message));
            renderer = UltralightRenderer.create();
            renderer.logMemoryUsage();
        } catch (Exception ignored){}
    }

    private void checkResources() {
        try {
            UltralightJava.extractNativeLibrary(resourcePath.toPath());
            UltralightGPUDriverNativeUtil.extractNativeLibrary(resourcePath.toPath());
            File resourcesZip = new File(resourcePath, "resources.zip");
            if (!resourcePath.exists()) resourcePath.mkdirs();
            if (resourcePath.listFiles().length >= 4) return;

            LoggerUtil.addTerminalMessage("Checking resources...");

            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("win")) os = "win";
            else if(os.contains("mac")) os = "mac";
            else if(os.contains("nix") || os.contains("nux") || os.contains("aix")) os = "linux";

            String downloadFile = "https://cloud.liquidbounce.net/LiquidBounce/ultralight_resources/" + ULTRALIGHT_NATIVE_VERSION + "/" + os + "-x64.zip";
            System.out.println(downloadFile);
            FileUtil.downloadFile(downloadFile, resourcesZip.toPath());
            FileUtil.extractZip(FileUtil.getZipInputStream(resourcesZip), resourcePath);
        } catch (Exception ignored){}
    }

    public void registerView(View view) {
        views.add(view);
    }

    public void unregisterView(View view) {
        views.remove(view);
        view.close();
    }

    public UltralightView ultraLight() {
        if(renderer == null) renderer.create();

        return renderer.createView(width, height,
                new UltralightViewConfig()
                        .initialDeviceScale(1.0)
                        .isTransparent(true)
        );
    }

    @EventInfo
    public void on2D(Event2D e) {
        boolean resized = false;

        if(width != Display.getWidth()) { width = Display.getWidth(); resized = true; }
        if(height != Display.getHeight()) { height = Display.getHeight(); resized = true;}

        ScaledResolution sr= new ScaledResolution(Minecraft.getMinecraft());
        scaledWidth = sr.scaledWidth;
        scaledHeight = sr.scaledHeight;
        factor = sr.scaleFactor;

        if(resized) { views.forEach(view -> view.resize(width, height)); }

        this.renderer.update();
        this.renderer.render();

        if(gcTimer.hasTimeReached(1000L)){
            views.forEach(View::gc);
            gcTimer.reset();
        }
    }
}
