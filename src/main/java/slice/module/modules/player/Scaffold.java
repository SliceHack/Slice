package slice.module.modules.player;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;
import slice.util.BlockUtil;
import slice.util.LoggerUtil;

import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ModuleInfo(name = "Scaffold", description = "Places blocks under you", category = Category.PLAYER)
public class Scaffold extends Module {

    NumberValue delay = new NumberValue("Delay", 100, 0, 6000, NumberValue.Type.LONG);
    BooleanValue swing = new BooleanValue("Swing", true);
    BooleanValue blockSpoofing = new BooleanValue("Block Spoofing", true);
    BooleanValue sprint = new BooleanValue("Sprint", true);

    private BlockData data;
    private int lastSlot;
    private float yaw = -1, pitch = -1, deltaYaw, deltaPitch;

    private final Block[] blacklisted = {
            Blocks.stone_stairs, Blocks.oak_stairs, Blocks.spruce_stairs, Blocks.birch_stairs, Blocks.jungle_stairs, Blocks.acacia_stairs, Blocks.dark_oak_stairs,
            Blocks.chest, Blocks.trapped_chest, Blocks.ender_chest,
            Blocks.enchanting_table, Blocks.anvil, Blocks.furnace,
            Blocks.lit_furnace, Blocks.crafting_table, Blocks.bed,
            Blocks.dropper, Blocks.dispenser, Blocks.hopper,
            Blocks.hopper, Blocks.piston, Blocks.sticky_piston,
            Blocks.piston_extension, Blocks.web
    };

    @Override
    public void onEnable() {
        lastSlot = mc.thePlayer.inventory.currentItem;
        deltaYaw = mc.thePlayer.rotationYaw;
        deltaPitch = mc.thePlayer.rotationPitch;
    }

    @Override
    public void onDisable() {
        if(blockSpoofing.getValue()) mc.thePlayer.sendQueue.addToSendNoEvent(new C09PacketHeldItemChange(lastSlot));
        else mc.thePlayer.inventory.currentItem = lastSlot;

        yaw = -1;
        pitch = -1;
    }

    @Override
    public void onUpdateNoToggle(EventUpdate event) {
        if(isEnabled()) return;

        deltaYaw = mc.thePlayer.rotationYaw;
        deltaPitch = mc.thePlayer.rotationPitch;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!sprint.getValue()) {
            mc.thePlayer.setSprinting(false);
            mc.gameSettings.keyBindSprint.pressed = false;
        }

        if(data != null) {
            final float[] rotations = getRotations();

            yaw = rotations[0];
            pitch = rotations[1];
        }

        if(yaw != -1 && pitch != -1) {
            e.setYaw(yaw);
            e.setPitch(pitch);
        }

        if(!e.isPre()) {
            data = getBlockData();
        }

        if(e.isPre()) {
            if(data == null) return;

            int slot = getBlocks();

            if(!blockSpoofing.getValue()) mc.thePlayer.inventory.currentItem = slot;

            if(slot == -1) return;

            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

            if(stack != null && stack.getItem() instanceof ItemBlock) {
                Block blockUnder = mc.theWorld.getBlockState(data.pos).getBlock();

                if(timer.hasReached(delay.getValue().longValue())) {
                    this.place(slot, stack);
                    timer.reset();
                }
            }

        }
    }

    public void place(int slot, ItemStack place) {
        if(blockSpoofing.getValue()) mc.thePlayer.sendQueue.addToSendNoEvent(new C09PacketHeldItemChange(slot));

        if(mc.theWorld.getBlockState(data.pos.add(data.pos.getX(), data.pos.getY(), data.pos.getZ())).getBlock().getMaterial() != Material.air) return;

        if(swing.getValue()) mc.thePlayer.swingItem();
//        else mc.thePlayer.sendQueue.addToSendNoEvent(new C0APacketAnimation());

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, place, data.pos, data.facing, new Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()));
    }

    public int getBlocks() {
        for(int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

            if(stack != null && stack.getItem() instanceof ItemBlock && !Arrays.asList(blacklisted).contains(((ItemBlock) stack.getItem()).getBlock()) && stack.stackSize > 0) {
                return i;
            }
        }

        return -1;
    }

    private BlockData getBlockData() {
        EnumFacing[] facings = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos pos = new BlockPos(mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN);
        EnumFacing[] facing = EnumFacing.values();

        for (EnumFacing enumFacing : facing) {
            if (mc.theWorld.getBlockState(pos.offset(enumFacing)).getBlock().getMaterial() == Material.air) continue;

            return new BlockData(pos.offset(enumFacing), facings[enumFacing.ordinal()]);
        }

        BlockPos[] positions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};

        for (BlockPos position : positions) {
            BlockPos currentPos = pos.add(position.getX(), 0, position.getZ());

            if (!(mc.theWorld.getBlockState(currentPos).getBlock() instanceof BlockAir)) continue;

            for (int i2 = 0; i2 < EnumFacing.values().length; ++i2) {

                if (mc.theWorld.getBlockState(currentPos.offset(EnumFacing.values()[i2])).getBlock().getMaterial() == Material.air)
                    continue;

                return new BlockData(currentPos.offset(EnumFacing.values()[i2]), facings[EnumFacing.values()[i2].ordinal()]);
            }
        }
        return null;
    }
    
    public float[] getRotations() {
        float[] rotations = BlockUtil.getDirectionToBlock(data.pos.getX(), data.pos.getY(), data.pos.getZ(), data.facing);
        float yaw = rotations[0], pitch = 90;

        float newYaw = MathHelper.wrapAngleTo180_float(deltaYaw - yaw), newPitch = MathHelper.wrapAngleTo180_float(deltaPitch - pitch);

        if (newYaw > 9F) newYaw = 9F;
        if (newYaw < -9F) newYaw = -9F;
        if (newPitch > 9F) newPitch = 9F;
        if (newPitch < -9F) newPitch = -9F;
        if (deltaPitch > 90) deltaPitch = 90;

        deltaYaw -= newYaw;
        deltaPitch -= newPitch;

        return new float[] {deltaYaw + new Random().nextInt(1), deltaPitch };
    }

    @AllArgsConstructor
    private static class BlockData {
        public BlockPos pos;
        public EnumFacing facing;

        public Vec3 getVector() {
            return new Vec3(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @AllArgsConstructor
    private static class BlockVector {
        public double x, y, z;
        private EnumFacing facing;

        public Vec3 getVector() {
            return new Vec3(x, y, z);
        }
    }

}
