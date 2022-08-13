package slice.cef;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import org.cef.browser.CefBrowser;
import slice.Slice;
import slice.notification.Notification;

@Getter @Setter
public class RequestHandler {
    public static RequestHandler INSTANCE;

    public CefBrowser browser;

    private boolean TargetHudShown;
    private boolean SessionHudShown;

    public RequestHandler(CefBrowser browser) {
        INSTANCE = this;
        this.browser = browser;
        this.setupInfo();
        sendJavascript("let iframe;");
        this.setupTargetHUD();
        TargetHudShown = true;
        RequestHandler.hideTargetHUD();
        this.setupSessionHUD();
        SessionHudShown = true;
        RequestHandler.hideSessionHUD();
    }

    public void sendJavascript(String js) {
        browser.executeJavaScript(js, null, 0);
    }

    public void setupInfo() {
        sendJavascript("document.querySelector(\".text\").innerHTML = \"" + Slice.NAME + " | " + Slice.VERSION + " | " + Slice.INSTANCE.discordName + "\";");
        sendJavascript("document.querySelector(\".box\").style.width = (document.querySelector(\".text\").offsetWidth)+ 50 + \"px\";");
    }

    public void createIframe(String path) {
        sendJavascript("document.body.insertAdjacentHTML('beforeend', '<iframe src=\"" + path + "\" frameborder=\"0\"></iframe>');");
    }

    public void setupTargetHUD() {
        createIframe("TargetHUD/index.html");
    }

    public static void updateTargetHUD(EntityLivingBase target) {
        if (!INSTANCE.TargetHudShown) return;
        double health = target.getHealth();
        double max = target.getMaxHealth();
        String name = target.getName();

        INSTANCE.sendJavascript("updateTargetHUD(" + health + ", " + max + ", \"" + name + "\");");
    }

    public static void hideTargetHUD() {
        if (!INSTANCE.TargetHudShown) return;
        INSTANCE.TargetHudShown = false;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='TargetHUD/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showTargetHUD() {
        if (INSTANCE.TargetHudShown) return;
        INSTANCE.TargetHudShown = true;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='TargetHUD/index.html']\").style.visibility = \"visible\";");
    }

    public void setupSessionHUD() {
        createIframe("SessionHUD/index.html");
    }

    public static void hideSessionHUD() {
        if (!INSTANCE.SessionHudShown) return;
        INSTANCE.SessionHudShown = false;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='SessionHUD/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showSessionHUD() {
        if (INSTANCE.SessionHudShown) return;
        INSTANCE.SessionHudShown = true;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='SessionHUD/index.html']\").style.visibility = \"visible\";");
    }

    public static void updateSessionHUD() {
        if (!INSTANCE.SessionHudShown) return;
        INSTANCE.sendJavascript("updateSessionHUD(\"" + Slice.INSTANCE.getDate() + "\", " + Slice.INSTANCE.getPlayers() + ", " + Slice.INSTANCE.getPing() + ", \"" + Slice.INSTANCE.getTotalPlayTime() + "\", \"" + Slice.INSTANCE.getPlayTime() + "\");");
    }

    public static void addNotification(Notification notification) {
        INSTANCE.sendJavascript("addNotification(\"" + notification.getTitle() + "\", \"" + notification.getMessage() + "\", " + notification.getSeconds() + ");");
    }
}
