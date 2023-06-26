package slice.module.modules.misc;

import lombok.NonNull;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

@ModuleInfo(name = "Stealer", description = "Steals from a chest", category = Category.MISC)
public class Stealer extends Module {

    NumberValue delay = new NumberValue("Delay", 1000, 50, 10000, NumberValue.Type.LONG);

    BooleanValue close = new BooleanValue("Close", true),
            check = new BooleanValue("Check", true),
            potions = new BooleanValue("Potions", true),
            food = new BooleanValue("Food", true),
            armor = new BooleanValue("Armor", true),
            tools = new BooleanValue("Tools", true),
            blocks = new BooleanValue("Blocks", true),
            other = new BooleanValue("Other", false);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!(mc.thePlayer.openContainer instanceof ContainerChest)) return;

        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
        String name = chest.getLowerChestInventory().getDisplayName().getFormattedText();

        if(check.getValue() && !name.contains("Chest")) return;

        for(Slot slot : chest.inventorySlots) {

            if(slot == null || !slot.getHasStack() || isEmpty(chest) || !shouldCollectItem(slot.getStack())) continue;

            if (timer.hasReached(delay.getValue().longValue())) {
                mc.playerController.windowClick(chest.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
                timer.reset();
            }

        }

        if(close.getValue() && isEmpty(chest)) {
            mc.thePlayer.closeScreen();
        }
    }

    public boolean shouldCollectItem(@NonNull ItemStack stack) {
        if(stack.getItem() instanceof ItemPotion)
            return potions.getValue();

        if(stack.getItem() instanceof ItemFood)
            return food.getValue();

        if(stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword)
            return tools.getValue();

        if(stack.getItem() instanceof ItemArmor)
            return armor.getValue();

        if(stack.getItem() instanceof ItemBlock)
            return blocks.getValue();

        return other.getValue();
    }

    public boolean isEmpty(@NonNull Container container) {
        for(int i = 0; i <= container.inventorySlots.size() - 36; i++) {
            if(!container.getSlot(i).getHasStack() || !shouldCollectItem(container.getSlot(i).getStack())) continue;

            return false;
        }
        return true;
    }
}
