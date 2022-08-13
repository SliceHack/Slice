package slice.module.modules.misc;

import net.minecraft.client.gui.inventory.GuiChest;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "DavidZar", description = "Automatically clicks the cookie thingy in david Zar's plugin", category = Category.MISC)
public class DavidZarCookieClicker extends Module {

    private int gui;

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(mc.currentScreen instanceof GuiChest) {
            GuiChest gui = (GuiChest) mc.currentScreen;


            int[] slots = {11, 10, 20, 19};
            for (int slot : slots) {
                if (mc.thePlayer.inventory.getStackInSlot(slot) == null) {
                    if (timer.hasTimeReached(200)) {
                        mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, 1, mc.thePlayer);
                        timer.reset();
                    }
                }
            }
        }
    }
}
