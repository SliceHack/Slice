package slice.module.modules.player;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.LoggerUtil;

import java.util.HashMap;

@ModuleInfo(name = "AutoArmor", description = "Automatically equips the best armor in your inventory", category = Category.PLAYER)
public class AutoArmor extends Module {

    @EventInfo
    public void onUpdate(EventUpdate e) {
    }

    public HashMap<ItemStack, Integer> getAllArmor() {
        HashMap<ItemStack, Integer> armor = new HashMap<>();

        for(int i = 0; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

            if(stack == null) continue;

            if(stack.getItem() instanceof ItemArmor) {
                armor.put(stack, i);
            }
        }

        return armor;
    }

}
