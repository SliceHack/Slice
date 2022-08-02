package slice.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.manager.EventManager;
import slice.util.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * for html rendering we need ultralight
 * this code is not written by us, we converted it from kotlin to java
 *
 * @author CCBlueX || @UnlegitMC
 * */
@Getter @Setter
@SuppressWarnings("all")
public class UltraLightEngine {

    private UltralightPlatform platform;
    private UltralightRenderer renderer;

    private int width = 0, height = 0, scaledWidth = 0, scaledHeight = 0, factor = 1;

    private Timer gcTimer = new Timer();

    private List<View> views = new ArrayList<>();

    private File ultralightPath = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Slice", "ultralight");
    private File resourcePath = new File(ultralightPath, "resources");
    private File pagesPath = new File(ultralightPath, "pages");
    private File cachePath = new File(ultralightPath, "cache");

    @SuppressWarnings("all")
    public UltraLightEngine() {
        exactResources();
        platform = UltralightPlatform.instance();

        if(!ultralightPath.exists()) ultralightPath.mkdirs();
        if(!resourcePath.exists()) resourcePath.mkdirs();
        if(!pagesPath.exists()) pagesPath.mkdirs();
        if(!cachePath.exists()) cachePath.mkdirs();

        platform.setConfig(new UltralightConfig()
                        .forceRepaint(false)
                        .resourcePath(resourcePath.getAbsolutePath())
                        .cachePath(cachePath.getAbsolutePath())
                        .fontHinting(FontHinting.SMOOTH)
        );
    }

    public void initSlice(EventManager eventManager) {
        eventManager.register(this);
    }

    @EventInfo
    public void on2D(Event2D e) {
        boolean resized=false;

        if(width != Display.getWidth()) {
            width = Display.getWidth();
            resized=true;
        }
        if(height != Display.getHeight()) {
            height = Display.getHeight();
            resized=true;
        }

        scaledWidth = e.getScaledResolution().getScaledWidth();
        scaledHeight = e.getScaledResolution().getScaledHeight();
        factor = e.getScaledResolution().getScaleFactor();

        if(resized) {
            views.forEach(view -> view.resize(width, height));
        }

        renderer.update();
        renderer.render();

        if(gcTimer.hasTimeReached(1000L)){
            views.forEach(View::gc);
            gcTimer.reset();
        }
    }

    public void exactResources() {
        try {
            UltralightJava.extractNativeLibrary(resourcePath.toPath());
            UltralightJava.load(resourcePath.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerView(View view) {
        views.add(view);
    }

    public void unregisterView(View view){
        views.remove(view);
        view.close();
    }

}
