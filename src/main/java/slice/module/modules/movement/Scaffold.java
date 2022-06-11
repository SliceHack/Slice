package slice.module.modules.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventSafeWalk;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "Scaffold", key = Keyboard.KEY_I, description = "Places blocks under you", category = Category.MOVEMENT)
public class Scaffold extends Module {

    NumberValue delay = new NumberValue("Delay", 0L, 0L, 10000L, NumberValue.Type.LONG);
    BooleanValue sameY = new BooleanValue("Same Y", false);
    BooleanValue safeWalk = new BooleanValue("SafeWalk", true);
    BooleanValue sprint = new BooleanValue("Sprint", true);

    public final List<Block> invalid;

    /** data */
    public BlockData data;
    public boolean placed;

    /** sameY */
    private double posY;

    private float yaw = 999F, pitch = 999F;

    public Scaffold() {
        this.invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    }

    public void onEnable() {
        posY = mc.thePlayer.posY;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            placed = false;

            data = getBlockData();

            if(sameY.getValue()) mc.thePlayer.posY = posY;

            if(yaw != 999 && pitch != 999) {
                e.setYaw(yaw);
                e.setPitch(pitch);
            }

            if(data != null) {
                if(!sprint.getValue())
                    mc.thePlayer.setSprinting(false);

                yaw = getNeededRotationsForBlock(data)[0];
                pitch = getNeededRotationsForBlock(data)[1];

                if(placed)
                    return;

                if(timer.hasReached(delay.getValue().longValue())) {
                    placed = place();
                    timer.reset();
                }
            }
        }
        if(event instanceof EventSafeWalk) {
            EventSafeWalk e = (EventSafeWalk) event;
            e.setCancelled(!safeWalk.getValue());
        }
    }

    public float[] getNeededRotationsForBlock(BlockData data) {
        double vecX = data.pos.getX() - mc.thePlayer.posX;
        double vecY = data.pos.getY() - mc.thePlayer.posY;
        double vecZ = data.pos.getZ() - mc.thePlayer.posZ;
        float yaw = (float) (Math.atan2(vecZ, vecX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(vecY, Math.sqrt(vecX * vecX + vecZ * vecZ)) * 180.0D / Math.PI);
        return new float[]{yaw, -pitch};
    }

    public boolean place() {
        BlockPos pos = data.getPos();
        EnumFacing facing = data.getFacing();
        if(getSlot() == -1) {
            moveBlocksToHotbar();
            return false;
        }
        Vec3i data = this.data.getFacing().getDirectionVec();
        mc.thePlayer.sendQueue.addToSendNoEvent(new C09PacketHeldItemChange(getSlot()));
        mc.thePlayer.sendQueue.addToSendNoEvent(new C0APacketAnimation());
        return mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.mainInventory[getSlot()], pos, facing, new Vec3(this.data.getPos().getX() + data.getX() * 0.5, this.data.getPos().getY() + data.getY() * 0.5, this.data.getPos().getZ() + data.getZ() * 0.5));
    }

    private BlockData getBlockData() {
        final EnumFacing[] invert = {
                EnumFacing.UP,
                EnumFacing.DOWN,
                EnumFacing.SOUTH,
                EnumFacing.NORTH,
                EnumFacing.EAST,
                EnumFacing.WEST };

        double yValue = 0.0;
        BlockPos pos = sameY.getValue() ? new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ).offset(EnumFacing.DOWN).add(0.0, yValue, 0.0) :
                new BlockPos(mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN).add(0.0, yValue, 0.0);

        final EnumFacing[] facingVals = EnumFacing.values();
        for (EnumFacing facingVal : facingVals) {
            if (mc.theWorld.getBlockState(pos.offset(facingVal)).getBlock().getMaterial() != Material.air) {
                return new BlockData(pos.offset(facingVal), invert[facingVal.ordinal()]);
            }
        }
        final BlockPos[] addons = {
                new BlockPos(-1, 0, 0),
                new BlockPos(1, 0, 0),
                new BlockPos(0, 0, -1),
                new BlockPos(0, 0, 1)
        };

        for (int i = addons.length, j = 0; j < i; ++j) {
            final BlockPos offsetPos = pos.add(addons[j].getX(), 0, addons[j].getZ());
            if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                for (int k = 0; k < EnumFacing.values().length; ++k) {
                    if (mc.theWorld.getBlockState(offsetPos.offset(EnumFacing.values()[k])).getBlock().getMaterial() != Material.air) {
                        return new BlockData(offsetPos.offset(EnumFacing.values()[k]), invert[EnumFacing.values()[k].ordinal()]);
                    }
                }
            }
        }
        return null;
    }

    public static int getEmptyHotbarSlot() {
        for (int k = 0; k < 9; ++k) {
            if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[k] == null) {
                return k;
            }
        }
        return -1;
    }

    private void moveBlocksToHotbar() {
        boolean added = false;

        if (getEmptyHotbarSlot() != -1) {
            for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
                if (k > 8 && !added) {
                    final ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[k];
                    if (itemStack != null && this.isValid(itemStack)) {
                        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, k, 0, 1, this.mc.thePlayer);
                        added = true;
                    }
                }
            }
        }
    }

    private boolean isValid(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock) {
            boolean isBad = false;
            final ItemBlock block = (ItemBlock) itemStack.getItem();
            for (Block value : this.invalid) {
                if (block.getBlock().equals(value)) {
                    isBad = true;
                    break;
                }
            }
            return !isBad;
        }
        return false;
    }

    private int getSlot() {
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && this.isValid(itemStack) && itemStack.stackSize >= 1) {
                return k;
            }
        }
        return -1;
    }

    /**
     * Class for BlockData
     *
     * @author Nick
     * */
    @Getter @Setter
    @AllArgsConstructor
    static class BlockData {
        private BlockPos pos;
        private EnumFacing facing;
    }
}
