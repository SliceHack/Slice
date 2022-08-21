package slice.module.modules.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.util.LoggerUtil;

import static net.minecraft.util.EnumFacing.*;

@ModuleInfo(name = "Scaffold", description = "Places blocks under you", category = Category.PLAYER)
public class Scaffold extends Module {

    private BlockData data;

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(e.isPre()) {
            BlockPos under = mc.thePlayer.getPosition().add(0, -1, 0);
            IBlockState state = state(under);

            if(state.getBlock() instanceof BlockAir) {
                data = getData(under);
            }
            return;
        }

        if(data == null) return;

        if(mc.thePlayer.getHeldItem() == null) return;
        place();
    }

    public void place() {
        if(data == null || data.pos == null) return;

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), data.getPos(), data.getFacing(), new Vec3(data.getPos().getX(), data.getPos().getY(), data.getPos().getZ()));
    }

    public BlockData getData(BlockPos pos) {
        EnumFacing facing = null;

        if(block(pos) != Blocks.air) return null;
        if(block(pos = pos.add(-1, 0, 0)) != Blocks.air) facing = EAST;
        if(block(pos = pos.add(1, 0, 0)) != Blocks.air) facing = WEST;
        if(block(pos = pos.add(0, 0, -1)) != Blocks.air) facing = SOUTH;
        if(block(pos = pos.add(0, 0, 1)) != Blocks.air) facing = NORTH;
        if(block(pos = pos.add(0, -1, 0)) != Blocks.air) facing = UP;
        if(block(pos = pos.add(0, 1, 0)) != Blocks.air) facing = DOWN;
        return new BlockData(pos, facing);
    }

    public Block block(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public IBlockState state(BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }


    @Getter @Setter
    @AllArgsConstructor
    static class BlockData {
        private BlockPos pos;
        private EnumFacing facing;
    }


}
