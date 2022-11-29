package slice.module.modules.combat;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import slice.event.data.EventInfo;
import slice.event.events.EventClientTick;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.PacketUtil;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleInfo(name = "AutoPot", description = "Automatically splash potions", category = Category.COMBAT)
public class AutoPot extends Module {

    NumberValue ticksValue = new NumberValue("Ticks", 20, 10, 40, NumberValue.Type.INTEGER);
    NumberValue healthValue = new NumberValue("Health", 10, 1, 20, NumberValue.Type.INTEGER);
    BooleanValue moveToHotBar = new BooleanValue("Move to HotBar", true);
    BooleanValue throwDown = new BooleanValue("Throw Potions Down", true);

    private int lastSlot = -1;
    private int ticks = 0;

    private boolean isPotting = false;

    private Runnable toRun;

    @Override
    public void onDisable() {
        if(lastSlot == -1) return;

        PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(lastSlot));
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(mc.thePlayer.fallDistance > 3) return;

        HashMap<ItemStack, Integer> pots = getAllSplashPotions();

        if(pots.isEmpty()) return;

        isPotting = true;

        pots.forEach((item, slot) -> {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[slot];
            ItemPotion potion = (ItemPotion) stack.getItem();

            if(shouldNotSplash(stack, potion)) return;
            if(toRun == null) isPotting = true;

            AtomicInteger newSlot = new AtomicInteger(slot);

            toRun = () -> {
                if(shouldNotSplash(stack, potion)) return; // do not remove this Dylan it prevents a bug

                int moved = moveToHotBar(newSlot.get());

                if(newSlot.get() > 8) newSlot.set(moved);

                lastSlot = mc.thePlayer.inventory.currentItem;

                mc.thePlayer.rotationPitchHead = throwDown.getValue() ? 90 : -90;
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(e.getX(), e.getY(), e.getZ(), e.getYaw(), throwDown.getValue() ? 90 : -90, e.isOnGround()));

                PacketUtil.sendPacket(new C09PacketHeldItemChange(newSlot.get()));
                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(stack));
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, mc.thePlayer.getPosition(), mc.thePlayer.getHorizontalFacing()));
                PacketUtil.sendPacket(new C09PacketHeldItemChange(lastSlot));
                lastSlot = -1;
            };
        });

        if(toRun != null && ticks >= ticksValue.getValue().intValue()) {
            toRun.run();
            isPotting = false;
            toRun = null;
            ticks = 0;
        }
    }

    @EventInfo
    public void onTick(EventClientTick e) {
        if(!isPotting) return;

        ticks++;
    }

    public int moveToHotBar(int slot) {
        int hotbarSlot = getHotBarSlot();
        if(hotbarSlot == -1 || slot < 8) return -1;
        if(!moveToHotBar.getValue()) return -1;

        mc.playerController.windowClick(0, slot, 0, 1, mc.thePlayer);
        mc.playerController.windowClick(0, hotbarSlot, 0, 1, mc.thePlayer);
        return hotbarSlot;
    }

    private int getHotBarSlot() {
        for(int i = 0; i < 9; i++) {
            if(mc.thePlayer.inventory.mainInventory[i] == null) return i;
        }
        return -1;
    }

    public boolean shouldNotSplash(ItemStack stack, ItemPotion potion) {
        List<PotionEffect> effects = potion.getEffects(stack);
        boolean shouldNotSplash = false;
        for(PotionEffect effect : effects) {
            if(!mc.thePlayer.isPotionActive(effect.getPotionID())) {
                // boolean healing
                boolean healing = effect.getPotionID() == Potion.heal.id;
                if(healing) {
                    shouldNotSplash = mc.thePlayer.getHealth() + effect.getAmplifier() + 1 <= healthValue.getValue().intValue();
                    continue;
                }


                shouldNotSplash = true;
            }
        }
        return !shouldNotSplash;
    }

    public HashMap<ItemStack, Integer> getAllSplashPotions() {
        HashMap<ItemStack, Integer> potions = new HashMap<>();
        for(int i = 0; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata())) {
                potions.put(stack, i);
            }
        }
        return potions;
    }

}
