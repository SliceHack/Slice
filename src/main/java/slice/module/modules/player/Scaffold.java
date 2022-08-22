package slice.module.modules.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0APacketAnimation;
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
            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            IBlockState state = mc.theWorld.getBlockState(pos);
            Block block = state.getBlock();

            if(block instanceof BlockAir) {
                data = getData(pos);

            }
            return;
        }
        if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), data.pos, data.facing, new Vec3(data.pos.x, data.pos.y, data.pos.z))) {
            mc.thePlayer.sendQueue.addToSendNoEvent(new C0APacketAnimation());
        }
    }

    public void place() {
        if(data == null || data.pos == null) return;

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), data.pos, data.facing, new Vec3(data.pos.x, data.pos.y, data.pos.z));
    }

    public BlockData getData(BlockPos pos) {
        EnumFacing facing;

        if(block(pos = pos.add(0, -1, 0)) != Blocks.air) facing = EnumFacing.UP;
        else if(block(pos = pos.add(-1, 0, 0)) != Blocks.air) facing = EnumFacing.EAST;
        else if(block(pos = pos.add(1, 0, 0)) != Blocks.air) facing = EnumFacing.WEST;
        else if(block(pos = pos.add(0, 0, -1)) != Blocks.air) facing = EnumFacing.SOUTH;
        else if(block(pos = pos.add(0, 0, 1)) != Blocks.air) facing = EnumFacing.NORTH;
        else if(block(pos = pos.add(0, 1, 0)) != Blocks.air) facing = EnumFacing.DOWN;
        else return null;

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
