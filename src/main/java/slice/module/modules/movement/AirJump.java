package slice.module.modules.movement;

import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

/**
 * @see net.minecraft.entity.EntityLivingBase#onLivingUpdate
 * */
@ModuleInfo(name = "AirJump", description = "Let's you jump in the air", category = Category.MOVEMENT)
public class AirJump extends Module {}