package slice.module.modules.player;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

import java.util.Arrays;

@ModuleInfo(name = "Scaffold", description = "Places blocks under you", category = Category.PLAYER)
public class Scaffold extends Module {

    NumberValue delay = new NumberValue("Delay", 100, 0, 6000, NumberValue.Type.LONG);

    private BlockData data;
    private int lastSlot;
    private float yaw = -1, pitch = -1;

    private final Block[] blacklisted = {
            Blocks.stone_stairs, Blocks.oak_stairs, Blocks.spruce_stairs, Blocks.birch_stairs, Blocks.jungle_stairs, Blocks.acacia_stairs, Blocks.dark_oak_stairs,
            Blocks.chest, Blocks.trapped_chest, Blocks.ender_chest,
            Blocks.enchanting_table, Blocks.anvil, Blocks.furnace,
            Blocks.lit_furnace, Blocks.crafting_table, Blocks.bed,
            Blocks.dropper, Blocks.dispenser, Blocks.hopper,
            Blocks.hopper, Blocks.piston, Blocks.sticky_piston,
            Blocks.piston_extension
    };

    @Override
    public void onEnable() {
        lastSlot = mc.thePlayer.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(lastSlot));
        yaw = -1;
        pitch = -1;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {

        if(yaw != -1 && pitch != -1) {
            e.setYaw(yaw);
            e.setPitch(pitch);
        }

        if(e.isPre()) {

            BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

            if(mc.theWorld.getBlockState(below).getBlock() instanceof BlockAir) {
                data = getBlockData(below);

                if(data != null) {
                    float[] rotations = getRotations(data.pos);

                    yaw = rotations[0];
                    pitch = rotations[1];
                }
            }
        }

        if(!e.isPre()) {
            if(data == null) return;

            int slot = getBlocks();

            if(slot == -1) return;

            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

            if(stack != null && stack.getItem() instanceof ItemBlock) {

                if(timer.hasReached(delay.getValue().longValue())) {
                    this.place(slot, stack);
                    timer.reset();
                }
            }

        }
    }

    private float[] getRotations(BlockPos pos) {
        double x = pos.getX() + 0.5 - mc.thePlayer.posX;
        double y = pos.getY() + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = pos.getZ() + 0.5 - mc.thePlayer.posZ;

        double dist = Math.sqrt(x * x + z * z);

        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);

        if(pitch < -90) pitch = -90;
        if(pitch > 90) pitch = 90;

        return new float[] { yaw, pitch };
    }

    public void place(int slot, ItemStack place) {
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
        mc.thePlayer.sendQueue.addToSendNoEvent(new C0APacketAnimation());

        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(data.pos, data.facing.getIndex(), place, data.pos.x, data.pos.y, data.pos.z));
    }

    public int getBlocks() {
        for(int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

            if(stack != null && stack.getItem() instanceof ItemBlock && !Arrays.asList(blacklisted).contains(((ItemBlock) stack.getItem()).getBlock())) {
                return i;
            }
        }

        return -1;
    }

    public BlockData getBlockData(@NonNull BlockPos pos) {
        BlockVector[] vectors = {
                new BlockVector(0, -1, 0, EnumFacing.UP),
                new BlockVector(-1, 0, 0, EnumFacing.EAST),
                new BlockVector(1, 0, 0, EnumFacing.WEST),
                new BlockVector(0, 0, -1, EnumFacing.SOUTH),
                new BlockVector(0, 0, 1, EnumFacing.NORTH),

                new BlockVector(-1, -1, 0, EnumFacing.UP),
                new BlockVector(1, -1, 0, EnumFacing.UP),
                new BlockVector(0, -1, -1, EnumFacing.UP),
                new BlockVector(0, -1, 1, EnumFacing.UP),

                // sideways
                new BlockVector(-1, 0, -1, EnumFacing.SOUTH),
                new BlockVector(-1, 0, 1, EnumFacing.NORTH),
                new BlockVector(1, 0, -1, EnumFacing.SOUTH),
                new BlockVector(1, 0, 1, EnumFacing.NORTH),
        };

        for(BlockVector vector : vectors) {
            if(mc.theWorld.getBlockState(pos.add(vector.x, vector.y, vector.z)).getBlock() != Blocks.air) {
                return new BlockData(pos.add(vector.x, vector.y, vector.z), vector.facing);
            }
        }

        return null;
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
