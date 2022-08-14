package org.cef.ccbluex;

import lombok.Getter;
import lombok.Setter;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.IProgressHandler;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.settings.GameSettings;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefBrowserCustom;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.scheme.SchemeResourceHandler;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import ru.vidtu.ias.utils.Request;
import slice.Slice;
import slice.cef.RequestHandler;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.events.EventUpdate;
import slice.event.events.EventUpdateLWJGL;
import slice.event.manager.EventManager;
import slice.gui.main.MainMenu;
import slice.gui.main.NewMainMenu;
import slice.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@SuppressWarnings("all")
public class CefRenderManager {

    private EventManager eventManager;

    private CefApp cefApp;
    private CefClient cefClient;
    private CefMessageRouter cefMessageRouter;

    private File dataDir = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Slice", "cef");
    private File cacheDir = new File(dataDir, "cef_cache");
    private List<String> cefArgs = new ArrayList<>();

    List<org.cef.browser.CefBrowserCustom> browsers = new ArrayList<>();

    public CefRenderManager(EventManager eventManager) {
        eventManager.register(this);
    }

    public void initializeAsync(IProgressHandler progressHandler) {
        new Thread(() -> initialize(progressHandler)).start();
    }

    public void initialize(IProgressHandler progressHandler) {
        try {
            System.out.println("Initializing CefRenderManager...");
            // data dir will create by CefAppBuilder
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

            // use jcef maven CefAppBuilder, it can download resources automatically
            CefAppBuilder builder = new CefAppBuilder();

            builder.setInstallDir(dataDir);
//            progressHandler.let { builder.setProgressHandler(it) };
            String[] args = cefArgs.toArray(new String[]{});
            int index = 0;
            for (String arg : cefArgs) {
                args[index] = arg;
                index++;
            }
            builder.addJcefArgs(args);
            builder.getCefSettings().windowless_rendering_enabled = true;
            builder.getCefSettings().locale = gameSettings.language;
            builder.getCefSettings().cache_path = cacheDir.getAbsolutePath();
            builder.getCefSettings().user_agent = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.7113.93 Safari/537.36 Java/1.8.0_191";

            cefApp = builder.build();
            cefClient = cefApp.createClient();
            cefMessageRouter = CefMessageRouter.create();
            cefMessageRouter.addHandler(new CefMessageRouterHandlerAdapter() {
                /**
                 * cef query can be used to contact browser and client
                 */
                @Override
                public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
                    callback.success("OK");

                    switch (request) {
                        case "READY":
                            new RequestHandler(browser);
                            LoggerUtil.addTerminalMessage("Browser Ready!");
                            break;
                        case "SinglePlayerScreen":
                            Minecraft.getMinecraft().displayGuiScreen(new GuiSelectWorld(null));
                            break;
                        case "MultiPlayerScreen":
                            Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(null));
                            break;
                        case "OptionsScreen":
                            Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(null, Minecraft.getMinecraft().gameSettings));
                            break;
                        case "Exit":
                            Minecraft.getMinecraft().shutdownMinecraftApplet();
                            break;

                    }

                    return super.onQuery(browser, frame, queryId, request, persistent, callback);
                }
            }, true);

            cefClient.addMessageRouter(cefMessageRouter);

            cefApp.registerSchemeHandlerFactory("resource", "", SchemeResourceHandler.build(new ResourceScheme()));

            CefApp.CefVersion version = cefApp.getVersion();
            LoggerUtil.addTerminalMessage("Cef Loaded (jcefVersion=" + version.getJcefVersion() + ", cefVersion=" + version.getCefVersion() + ", chromeVersion=" + version.getChromeVersion() + ")");
        } catch (UnsupportedPlatformException | IOException | InterruptedException | CefInitializationException e){
            e.printStackTrace();
        }
    }

    public void stop() {
        cefApp.dispose();
    }

    @EventInfo
    public void on2D(Event2D e) {
        cefApp.doMessageLoopWork(0L);
        browsers.forEach(CefBrowserCustom::mcefUpdate);
    }

    @EventInfo
    public void onUpdateDisplay(EventUpdateLWJGL e) {
        cefApp.doMessageLoopWork(0L);
        browsers.forEach(CefBrowserCustom::mcefUpdate);
    }
}
