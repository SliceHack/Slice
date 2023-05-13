package slice.module.modules.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "SumoFences", description = "Creates fences to stop you from going off the edge in hypixel.", category = Category.WORLD)
public class SumoFences extends Module {

    NumberValue height = new NumberValue("height", 1D, 1D, 4D, NumberValue.Type.DOUBLE);
    double lastHeight;

    private static final List<BlockPos> fenceLocations = Arrays.asList(new BlockPos(9, 65, -2), new BlockPos(9, 65, -1), new BlockPos(9, 65, 0), new BlockPos(9, 65, 1), new BlockPos(9, 65, 2), new BlockPos(9, 65, 3), new BlockPos(8, 65, 3), new BlockPos(8, 65, 4), new BlockPos(8, 65, 5), new BlockPos(7, 65, 5), new BlockPos(7, 65, 6), new BlockPos(7, 65, 7), new BlockPos(6, 65, 7), new BlockPos(5, 65, 7), new BlockPos(5, 65, 8), new BlockPos(4, 65, 8), new BlockPos(3, 65, 8), new BlockPos(3, 65, 9), new BlockPos(2, 65, 9), new BlockPos(1, 65, 9), new BlockPos(0, 65, 9), new BlockPos(-1, 65, 9), new BlockPos(-2, 65, 9), new BlockPos(-3, 65, 9), new BlockPos(-3, 65, 8), new BlockPos(-4, 65, 8), new BlockPos(-5, 65, 8), new BlockPos(-5, 65, 7), new BlockPos(-6, 65, 7), new BlockPos(-7, 65, 7), new BlockPos(-7, 65, 6), new BlockPos(-7, 65, 5), new BlockPos(-8, 65, 5), new BlockPos(-8, 65, 4), new BlockPos(-8, 65, 3), new BlockPos(-9, 65, 3), new BlockPos(-9, 65, 2), new BlockPos(-9, 65, 1), new BlockPos(-9, 65, 0), new BlockPos(-9, 65, -1), new BlockPos(-9, 65, -2), new BlockPos(-9, 65, -3), new BlockPos(-8, 65, -3), new BlockPos(-8, 65, -4), new BlockPos(-8, 65, -5), new BlockPos(-7, 65, -5), new BlockPos(-7, 65, -6), new BlockPos(-7, 65, -7), new BlockPos(-6, 65, -7), new BlockPos(-5, 65, -7), new BlockPos(-5, 65, -8), new BlockPos(-4, 65, -8), new BlockPos(-3, 65, -8), new BlockPos(-3, 65, -9), new BlockPos(-2, 65, -9), new BlockPos(-1, 65, -9), new BlockPos(0, 65, -9), new BlockPos(1, 65, -9), new BlockPos(2, 65, -9), new BlockPos(3, 65, -9), new BlockPos(3, 65, -8), new BlockPos(4, 65, -8), new BlockPos(5, 65, -8), new BlockPos(5, 65, -7), new BlockPos(6, 65, -7), new BlockPos(7, 65, -7), new BlockPos(7, 65, -6), new BlockPos(7, 65, -5), new BlockPos(8, 65, -5), new BlockPos(8, 65, -4), new BlockPos(8, 65, -3), new BlockPos(9, 65, -3));
    private static final IBlockState fence = Blocks.oak_fence.getDefaultState();

    @EventInfo
    public void onUpdate(EventUpdate e) {
        for (BlockPos blockpos : fenceLocations) {
            for (int i = 0; i < height.getValue().doubleValue(); i++) {
                BlockPos blockpos2 = new BlockPos(blockpos.x, blockpos.y + i, blockpos.z);
                mc.theWorld.setBlockState(blockpos2, fence);
            }
        }
        if (lastHeight != height.getValue().doubleValue()) {
            onDisable();
        }

        lastHeight = height.getValue().doubleValue();
    }

    @Override
    public void onDisable() {
        for (BlockPos blockpos : fenceLocations) {
            for (int i = 0; i < 4; i++) {
                BlockPos blockpos2 = new BlockPos(blockpos.x, blockpos.y + (i), blockpos.z);
                if (mc.theWorld.getBlockState(blockpos2) == fence) {
                    mc.theWorld.setBlockToAir(blockpos2);
                }
            }
        }
    }

}
