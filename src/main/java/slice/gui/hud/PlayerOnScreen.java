package slice.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import slice.util.LoggerUtil;
import slice.util.MathUtil;

public class PlayerOnScreen {

    public void draw() {
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        yaw /= 2.5f;

        GuiInventory.drawEntityOnScreen(120, 80, 25, yaw, 0, Minecraft.getMinecraft().thePlayer);
    }
}
