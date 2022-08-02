package slice.module.modules.movement;

import net.minecraft.util.BlockPos;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.KeyUtil;
import slice.util.MoveUtil;
import slice.Slice;

@ModuleInfo(name = "TargetStrafe", description = "Sprints for you", category = Category.MOVEMENT)
public class TargetStrafe extends Module {
    @SuppressWarnings("unused")
    ModeValue mode = new ModeValue("Mode", "LiquidBounce", "LiquidBounce", "Dev");
    NumberValue range = new NumberValue("Distance", 3, 0.5, 8, NumberValue.Type.FLOAT);
    NumberValue speed = new NumberValue("Speed", 0.28, 0.1, 1, NumberValue.Type.FLOAT);
    NumberValue fov = new NumberValue("FOV", 180, 30, 180, NumberValue.Type.INTEGER);
    BooleanValue onMove = new BooleanValue("On Move", true);
    BooleanValue autojump = new BooleanValue("Auto Jump", true);

    boolean strafing = false;
    int direction = 1;

    @EventInfo
    public void onUpdate(EventUpdate e) {

        if (strafing && mc.thePlayer.onGround && autojump.getValue()) {
            mc.thePlayer.jump();
        }

        if (MoveUtil.isMoving()) {
            strafing = false;
            if ((Slice.INSTANCE.target != null) && (KeyUtil.forward().isKeyDown() || !onMove.getValue()) && !KeyUtil.moveKeys()[4].isKeyDown()) {
                double distance = Math.sqrt(Math.pow(mc.thePlayer.posX - Slice.INSTANCE.target.posX, 2) + Math.pow(mc.thePlayer.posZ - Slice.INSTANCE.target.posZ, 2));
                double strafeYaw = Math.atan2(Slice.INSTANCE.target.posZ - mc.thePlayer.posZ, Slice.INSTANCE.target.posX - mc.thePlayer.posX);
                double yaw = strafeYaw- (0.5 * Math.PI);
                double[] predict = {
                        Slice.INSTANCE.target.posX + (2 * (Slice.INSTANCE.target.posX - Slice.INSTANCE.target.lastTickPosX)),
                        Slice.INSTANCE.target.posZ + (2 * (Slice.INSTANCE.target.posZ - Slice.INSTANCE.target.lastTickPosZ))
                };

                if ((distance - speed.getValue().floatValue() > range.getValue().floatValue()) || Math.abs(((((yaw * 180 / Math.PI - mc.thePlayer.rotationYaw) % 360) + 540) % 360) - 180) > fov.getValue().intValue() || !isAboveGround(predict[0], Slice.INSTANCE.target.posY, predict[1])) return;
                double encirclement = Math.max(distance - range.getValue().doubleValue(), -speed.getValue().doubleValue());
                double encirclementX = -Math.sin(yaw) * encirclement;
                double encirclementZ = Math.cos(yaw) * encirclement;
                double strafeX = -Math.sin(strafeYaw) * speed.getValue().doubleValue() * direction;
                double strafeZ = Math.cos(strafeYaw) * speed.getValue().doubleValue() * direction;

                if (mc.thePlayer.onGround && (!isAboveGround(mc.thePlayer.posX + encirclementX + (2 * strafeX), mc.thePlayer.posY, mc.thePlayer.posZ + encirclementZ + (2 * strafeZ)) || mc.thePlayer.isCollidedHorizontally)) {
                    direction *= -1;
                    strafeX *= -1;
                    strafeZ *= -1;
                }

                mc.thePlayer.motionX = (encirclementX + strafeX);
                mc.thePlayer.motionZ = (encirclementZ + strafeZ);
                strafing = true;
            }
        }

    }

    private Boolean isAboveGround(Double x, Double y, Double z) {
        for (int i = (int) Math.ceil(y); (y - 5) < i--;) if (mc.theWorld.isAirBlock(new BlockPos(x, i, z))) return true;
        return false;
    }

}