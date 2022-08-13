package slice.gui.hud.legacy;

import net.minecraft.client.Minecraft;
import slice.cef.RequestHandler;
import slice.util.Timer;

public class SessionHUD {
    Timer timer = new Timer();

    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int mouseX, int mouseY) {
        RequestHandler.showSessionHUD();

        if (timer.hasReached(1000)) {
            RequestHandler.updateSessionHUD();
            timer.reset();
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {}
}
