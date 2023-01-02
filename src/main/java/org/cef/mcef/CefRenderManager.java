package org.cef.mcef;

import lombok.Getter;
import lombok.Setter;
import me.friwi.jcefmaven.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Session;
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
import slice.event.manager.EventManager;
import slice.gui.alt.HTMLAlt;
import slice.module.Module;
import slice.setting.Setting;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.account.LoginUtil;
import slice.util.account.microsoft.MicrosoftAccount;
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
                    "--disable-extensions"
            );
            builder.getCefSettings().locale = gameSettings.language;
            builder.getCefSettings().cache_path = cacheDir.getAbsolutePath();
            builder.getCefSettings().user_agent = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.7113.93 Safari/537.36 Java/1.8.0_191";

            cefApp = builder.build();
            cefClient = cefApp.createClient();
            cefMessageRouter = CefMessageRouter.create();
            final HTMLAlt[] htmlAlt = new HTMLAlt[1];
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
                            mc.displayGuiScreen(htmlAlt[0] = new HTMLAlt());
                            break;
                        case "VersionScreen":
                            mc.displayGuiScreen(new GuiProtocolSelector(screen));
                            break;
                        case "Exit":
                            System.exit(0);
                            break;
                        case "Init":
                            Slice.INSTANCE.getClickGui().queryInit();
                            break;
                        case "CloseGui":
                            mc.displayGuiScreen(null);
                            break;
                        case "AltManagerReady":
                            HTMLAlt alt = htmlAlt[0];
                            if (Slice.INSTANCE.currentEmail != null && Slice.INSTANCE.currentPassword != null) {
                                alt.getCefBrowser().executeJavaScript("addAccount(\"" + mc.session.getUsername() + "\", \"" + Slice.INSTANCE.currentEmail + "\", \"" + Slice.INSTANCE.currentPassword + "\")", null, 0);
                            } else {
                                alt.getCefBrowser().executeJavaScript("addAccount(\"" + mc.session.getUsername() + "\", \"" + mc.session.getUsername() + "\", \"" + mc.session.getUsername() + "\")", null, 0);
                            }

                            alt.getCefBrowser().executeJavaScript("addAccount(\"" + mc.session.getUsername() + "\")", null, 0);
                            break;

                    }

                    if(request.startsWith("Login ")) {
                        String[] args = request.substring(6).split(":");
                        String email = args[0];
                        String password = args[1];
                        MicrosoftAccount account = LoginUtil.loginMicrosoftNoSetSession(email, password);
                        if (account != null) {
                            browser.executeJavaScript(String.format("addAccount(\"%s\",\"%s\", \"%s\")", account.getProfile().getName(), email, password), browser.getURL(), 0);
                        }
                    }

                    if(request.startsWith("RealLogin ")) {
                        String[] args = request.substring(10).split(":");
                        String email = args[0];
                        String password = args[1];
                        if (!email.contains("@")) {
                            Session account = LoginUtil.loginOffline(email);
                            browser.executeJavaScript(String.format("setCurrentAccount(\"%s\")", account.getProfile().getName()), browser.getURL(), 0);
                        } else {
                            MicrosoftAccount account = LoginUtil.loginMicrosoft(email, password);
                            if (account != null) {
                                browser.executeJavaScript("setCurrentAccount(\"" + account.getProfile().getName() + "\")", browser.getURL(), 0);
                                Slice.INSTANCE.currentEmail = email;
                                Slice.INSTANCE.currentPassword = password;
                            }
                        }
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
