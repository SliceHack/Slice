package slice.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import slice.event.data.EventInfo;
import slice.event.events.EventAttack;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.LoggerUtil;

@ModuleInfo(name = "AutoTool", description = "Uses the correct tool for you.", category = Category.PLAYER)
public class AutoTool extends Module {

    @EventInfo
    public void onAttack(EventAttack e) {
        if (mc.thePlayer.isEating()) return;
        this.bestSword();
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if (!Mouse.isButtonDown(0)) return;
        if (!(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) return;
        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
        if (mc.thePlayer.getDistance(blockPos) > 3) return;
        mc.thePlayer.inventory.currentItem = this.getBestTool(blockPos);
        mc.playerController.updateController();
    }

    public void bestSword() {
        int bestSlot = 0;
        double bestDamage = 0;

        for (int hotbarSlots = 36; hotbarSlots < 45; hotbarSlots++) {
            if (mc.thePlayer.inventoryContainer.inventorySlots.toArray()[hotbarSlots] == null) break;
            ItemStack slot = mc.thePlayer.inventoryContainer.getSlot(hotbarSlots).getStack();
            if (slot == null || !(slot.getItem() instanceof ItemSword)) break;
            double damage = ((AttributeModifier) slot.getAttributeModifiers().get("generic.attackDamage").toArray()[0]).getAmount() + (double) this.getEnchantment(slot, Enchantment.sharpness) * 1.25 + (double) this.getEnchantment(slot, Enchantment.fireAspect);
            if (damage > bestDamage) {
                bestSlot = hotbarSlots - 36;
                bestDamage = damage;
            }
        }

        mc.thePlayer.inventory.currentItem = bestSlot;
        mc.playerController.updateController();
    }

    public int getEnchantment(ItemStack itemStack, Enchantment enchantment) {
        if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags())
            return 0;
        for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (tagCompound.hasKey("ench") && tagCompound.getShort("ench") == enchantment.effectId || tagCompound.hasKey("id") && tagCompound.getShort("id") == enchantment.effectId) {
                return tagCompound.getShort("lvl");
            }
        }

        return 0;
    }

    public int getBestTool(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        int slot = 0;
        float damage = 0.1F;

        for (int index = 36; index < 45; ++index) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack != null && block != null && itemStack.getItem().getStrVsBlock(itemStack, block) > damage) {
                slot = index - 36;
                damage = itemStack.getItem().getStrVsBlock(itemStack, block);
            }
        }

        if (damage > 0.1F) {
            return slot;
        } else {
            return mc.thePlayer.inventory.currentItem;
        }
    }
}
