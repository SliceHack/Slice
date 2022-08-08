package slice.cef;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import org.cef.browser.CefBrowser;
import slice.Slice;

@Getter @Setter
public class RequestHandler {
    public static RequestHandler INSTANCE;

    public final CefBrowser browser;

    public RequestHandler(CefBrowser browser) {
        INSTANCE = this;
        this.browser = browser;
        this.setupInfo();
        sendJavascript("let iframe;");
        this.setupTargetHUD();
        this.setupSessionHUD();
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
        double health = target.getHealth();
        double max = target.getMaxHealth();
        String name = target.getName();

        INSTANCE.sendJavascript("updateTargetHUD(" + health + ", " + max + ", \"" + name + "\");");
    }

    public static void hideTargetHUD() {
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='TargetHUD/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showTargetHUD() {
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='TargetHUD/index.html']\").style.visibility = \"visible\";");
    }

    public void setupSessionHUD() {
        createIframe("SessionHUD/index.html");
    }

    public static void hideSessionHUD() {
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='SessionHUD/index.html']\").style.visibility = \"hidden\";");
    }

    public static void showSessionHUD() {
        INSTANCE.sendJavascript("document.querySelector(\"iframe[src='SessionHUD/index.html']\").style.visibility = \"visible\";");
    }



}
