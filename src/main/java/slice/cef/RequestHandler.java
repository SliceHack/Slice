package slice.cef;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import org.cef.browser.CefBrowser;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.gui.main.HTMLMainMenu;
import slice.module.Module;
import slice.notification.Notification;
import slice.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@Getter @Setter
public class RequestHandler {

    public static RequestHandler INSTANCE;

    public CefBrowser browser;

    private boolean targetHUDShown, sessionHUDShown;

    public RequestHandler(CefBrowser browser) {
        if(INSTANCE != null) return;

        INSTANCE = this;
        this.browser = browser;
        this.setupInfo();
        sendJavascript("let iframe;");
        this.setupTargetHUD();
        sendJavascript("let arraylist;");
        this.setupArrayList();

        targetHUDShown = true;
        RequestHandler.hideTargetHUD();
        this.setupSessionHUD();
        sessionHUDShown = true;
        RequestHandler.hideSessionHUD();

    }

    public static void addToArrayList(String text) {
        if(INSTANCE == null) return;

        INSTANCE.sendJavascript("addToArrayList(\"" + text + "\");");
    }

    public static void removeFromArrayList(String text) {
        if(INSTANCE == null) return;

        INSTANCE.sendJavascript("removeFromArrayList(\"" + text + "\");");
    }

    public static void renameFromArrayList(String value, String newValue) {
        if(INSTANCE == null) return;

        INSTANCE.sendJavascript("renameFromArrayList(\"" + value + "\", \"" + newValue + "\");");
    }

    public static void setBPSVisible(boolean show) {
        if(INSTANCE == null) return;

        INSTANCE.sendJavascript("setBPSVisible(" + show + ");");
    }

    public static void setBPS(double bps) {
        if(INSTANCE == null) return;

        INSTANCE.sendJavascript("setBPS(\"" + bps + "\");");
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
        createIframe("targethud/index.html");
    }

    public static void updateTargetHUD(EntityLivingBase target) {
        if (!INSTANCE.targetHUDShown) return;
        double health = target.getHealth();
        double max = target.getMaxHealth();
        String name = target.getName();

        INSTANCE.sendJavascript("updateTargetHUD(" + health + ", " + max + ", \"" + name + "\");");
    }

    public static void hideTargetHUD() {
        if(INSTANCE == null) return;
        if (!INSTANCE.targetHUDShown) return;

        INSTANCE.targetHUDShown = false;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='targethud/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showTargetHUD() {
        if(INSTANCE == null) return;
        if (INSTANCE.targetHUDShown) return;

        INSTANCE.targetHUDShown = true;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='targethud/index.html']\").style.visibility = \"visible\";");
    }

    public void setupSessionHUD() {
        createIframe("sessionhud/index.html");
    }
    public void setupArrayList() {
        createIframe("arraylist/index.html");
    }
    public void removeArrayList() {
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='arraylist/index.html']\").remove();");
    }

    public static void hideSessionHUD() {
        if (!INSTANCE.sessionHUDShown) return;
        INSTANCE.sessionHUDShown = false;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='sessionhud/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showSessionHUD() {
        if (INSTANCE.sessionHUDShown) return;
        INSTANCE.sessionHUDShown = true;
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='sessionhud/index.html']\").style.visibility = \"visible\";");
    }

    public static void updateSessionHUD() {
        if (!INSTANCE.sessionHUDShown) return;
        INSTANCE.sendJavascript("updateSessionHUD(\"" + Slice.INSTANCE.getDate() + "\", " + Slice.INSTANCE.getPlayers() + ", " + Slice.INSTANCE.getPing() + ", \"" + Slice.INSTANCE.getTotalPlayTime() + "\", \"" + Slice.INSTANCE.getPlayTime() + "\");");
    }

    public static void addNotification(Notification notification) {
        INSTANCE.sendJavascript("addNotification(\"" + notification.getTitle() + "\", \"" + notification.getMessage() + "\", " + notification.getSeconds() + ");");
    }
}
