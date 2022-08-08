package slice.gui.hud.legacy;

import net.minecraft.client.Minecraft;
import slice.cef.RequestHandler;

public class SessionHUD {
    Minecraft mc = Minecraft.getMinecraft();

    public void draw(int mouseX, int mouseY) {
        RequestHandler.showSessionHUD();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY) {}
}
