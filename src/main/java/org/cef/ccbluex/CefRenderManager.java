package org.cef.ccbluex;

import lombok.Getter;
import lombok.Setter;
import me.friwi.jcefmaven.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
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
import slice.Slice;
import slice.cef.RequestHandler;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.events.EventGuiRender;
import slice.event.events.EventUpdateLWJGL;
import slice.event.manager.EventManager;
import slice.gui.alt.GuiAlt;
import slice.gui.main.HTMLMainMenu;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.account.LoginUtil;
import viamcp.gui.GuiProtocolSelector;

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
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

            CefAppBuilder builder = new CefAppBuilder();

            builder.setInstallDir(dataDir);
            progressHandler.handleProgress(EnumProgress.INITIALIZED, 1f);
            builder.addJcefArgs(
                    "--disable-web-security",
                    "--allow-file-access-from-files",
                    "--disable-plugins",
                    "--disable-extensions",
                    "--disable-gpu",
                    "--disable-gpu-compositing",
                    "--disable-gpu-vsync",
                    "--disable-gpu-shader-disk-cache",
                    "--disable-gpu-driver-bug-workarounds",
                    "--disable-gpu-program-cache",
                    "--disable-gpu-sandbox",
                    "--disable-gpu-watchdog",
                    "--disable-gpu-rasterization",
                    "--disable-gpu-early-init",
                    "--disable-gpu-memory-buffer-compositor-resources",
                    "--disable-gpu-memory-buffer-video-frames"
            );
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

                    GuiScreen screen = Minecraft.getMinecraft().currentScreen;
                    Minecraft mc = Minecraft.getMinecraft();
                    GameSettings gameSettings = mc.gameSettings;

                    switch (request) {
                        case "READY":
                            new RequestHandler(browser);
                            break;
                        case "SinglePlayerScreen":
                            mc.displayGuiScreen(new GuiSelectWorld(screen));
                            break;
                        case "MultiPlayerScreen":
                            mc.displayGuiScreen(new GuiMultiplayer(screen));
                            break;
                        case "OptionsScreen":
                            mc.displayGuiScreen(new GuiOptions(screen, gameSettings));
                            break;
                        case "AltManagerScreen":
                            mc.displayGuiScreen(new GuiAlt(screen));
                            break;
                        case "VersionScreen":
                            mc.displayGuiScreen(new GuiProtocolSelector(screen));
                            break;
                        case "Exit":
                            mc.shutdownMinecraftApplet();
                            break;
                        case "Init":
                            Slice.INSTANCE.getClickGui().queryInit();
                            break;
                        case "CloseGui":
                            mc.displayGuiScreen(null);
                            break;

                    }

                    if(request.startsWith("Login ")) {
                        String[] args = request.substring(6).split(":");
                        String email = args[0];
                        String password = args[1];
                        LoginUtil.loginMicrosoft(email, password);
                    }

                    String[] r = request.split(" ");
                    if(Slice.INSTANCE.getModuleManager().getModule(r[0]) != null) {
                        Module module = Slice.INSTANCE.getModuleManager().getModule(r[0]);

                        if(r.length >= 3) {
                            Setting setting = module.getSetting(r[1]);

                            if (setting != null) {
                                if (setting instanceof ModeValue) ((ModeValue) setting).setValue(r[2]);
                                if (setting instanceof BooleanValue) ((BooleanValue) setting).setValue(Boolean.parseBoolean(r[2]));
                                if (setting instanceof NumberValue) ((NumberValue) setting).setValue(Double.parseDouble(r[2]));
                            }
                        }
                        if(r.length == 2) {
                            module.toggle();
                        }
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
        browsers.forEach(CefBrowserCustom::mcefUpdate);
        cefApp.doMessageLoopWork(0L);
    }


}
