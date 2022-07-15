package slice.module.modules.world;

import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import slice.event.Event;
import slice.event.events.EventMouse;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;

@ModuleInfo(name = "Phase", description = "Removes blocks on right click.", category = Category.WORLD)
public class Phase extends Module {

    ModeValue mode = new ModeValue("Mode", "HCF", "HCF", "Dev");

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (Mouse.isButtonDown(1)) {
                switch(mode.getValue()) {
                    case "HCF":
                        BlockPos blockPos = mc.objectMouseOver.getBlockPos();

                        if (mc.thePlayer.getDistance(blockPos) > 3) break;

                        mc.theWorld.setBlockToAir(blockPos);
                        break;
                    case "Dev":
                    default:
                        break;
                }
            }
        }
    }


}