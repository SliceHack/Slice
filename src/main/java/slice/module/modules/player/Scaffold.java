package slice.module.modules.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Scaffold", description = "Places blocks under you", category = Category.PLAYER)
public class Scaffold extends Module {

    private BlockData data;

    @EventInfo
    public void onUpdate(EventUpdate e) {
        // TODO: Implement rotations

        if(e.isPre()) {
            if(data == null || data.pos == null) {
                data = getBlockData();
            }

            return;
        }

        if(data != null) {

            if(mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {

                if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), data.pos, data.facing, new Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()))) {
                    mc.thePlayer.swingItem();
                    data = null;
                }

            }
        }

    }

    public BlockData getBlockData() {
        BlockPos pos = mc.thePlayer.getPosition();
        BlockData blockData = new BlockData(null, null);

        int[] values = {
                0, -1, 0,
                -1, 0, 0,
                1, 0, 0,
                0, 0, -1,
                0, 0, 1
        };

        int v = 0;
        for(int i = 0; i < values.length; i += 3) {
            v++;

            BlockPos blockPos = pos.add(values[i], values[i + 1], values[i + 2]);


            if(mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.air) {

                if(v == 1) blockData.facing = EnumFacing.UP;
                else if(v == 2) blockData.facing = EnumFacing.DOWN;
                else if(v == 3) blockData.facing = EnumFacing.WEST;
                else if(v == 4) blockData.facing = EnumFacing.SOUTH;
                else if(v == 5) blockData.facing = EnumFacing.NORTH;

                blockData.pos = blockPos;
            }
        }
        return blockData;
    }

    @AllArgsConstructor
    private static class BlockData {
        public BlockPos pos;
        public EnumFacing facing;
    }

}
