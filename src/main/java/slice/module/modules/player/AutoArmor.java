package slice.module.modules.player;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.PacketUtil;

import java.util.TimerTask;
import java.util.Timer;

@ModuleInfo(name = "AutoArmor", description = "Automatically equips the best armor in your inventory", category = Category.PLAYER)
public class AutoArmor extends Module {

    NumberValue delay = new NumberValue("Delay", 100L, 0L, 1000L, NumberValue.Type.LONG);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        int[] armor = bestArmor();
        for(int i : armor) {
            if(i == -1) continue;
            if(i > 35) continue;
            equip(i);
        }
    }

    public int[] bestArmor() {
        int[] armor = new int[4];
        for(int i = 0; i < 4; i++) armor[i] = -1;

        for(int i = 0; i <= 39; i++) {
            if(mc.thePlayer.inventory.getStackInSlot(i) == null) continue;
            Item item = mc.thePlayer.inventory.getStackInSlot(i).getItem();

            if(!(item instanceof ItemArmor)) { armor[i] = -1; continue; }

            ItemArmor armorItem = (ItemArmor) item;
            int piece = armor[armorItem.armorType];

            if(piece == -1) { armor[armorItem.armorType] = i; continue; }

            ItemStack foundAgain = mc.thePlayer.inventory.getStackInSlot(i), current = mc.thePlayer.inventory.getStackInSlot(armor[armorItem.armorType]);
            if (isBetter(current, foundAgain)) {
                drop(armor[armorItem.armorType]);
                armor[armorItem.armorType] = i;
            }
        }
        return armor;
    }

    public void equip(int slot) {
        if(slot == -1) return;
        if(slot > 35) return;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(0, 8 - mc.thePlayer.inventory.currentItem, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, 1, mc.thePlayer);
            }
        }, delay.getValue().longValue());
    }

    public void unEquip(int slot) {
        if(slot == -1) return;
        if(slot < 35) return;

        mc.playerController.windowClick(0, slot, 0, 1, mc.thePlayer);
        mc.playerController.windowClick(0, 45, 0, 1, mc.thePlayer);
        mc.playerController.windowClick(0, slot, 0, 1, mc.thePlayer);
    }

    public void drop(int slot) {
        if(slot == -1) return;
        if(slot < 35) return;

        mc.playerController.windowClick(0, slot, 0, 0, mc.thePlayer);
        PacketUtil.sendPacket(new C0DPacketCloseWindow());
    }

    public boolean isBetter(ItemStack stack, ItemStack stack2) {
        if(EnchantmentHelper.hasProtection(stack) && !EnchantmentHelper.hasProtection(stack2)) return true;
        if(EnchantmentHelper.hasProtection(stack2) && !EnchantmentHelper.hasProtection(stack)) return false;
        return getProtection(stack) > getProtection(stack2);
    }

    public int getProtection(ItemStack stack) {
        if(EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) > 0) return EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
        if(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) > 0) return EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
        if(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) > 0) return EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
        return Math.max(EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack), 0);
    }

}
