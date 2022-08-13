package slice.module.modules.combat;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;
import slice.util.KeyUtil;
import slice.Slice;

@ModuleInfo(name = "PvPBot", category = Category.COMBAT)
@Getter
public class PvPBot extends Module {

    NumberValue rangeToPlayer = new NumberValue("Range To Stop", 3.14D, 1.0D, 3.14D, NumberValue.Type.DOUBLE);
    NumberValue cps = new NumberValue("CPS", 10, 1, 20, NumberValue.Type.INTEGER);

    private EntityLivingBase target;

    private float deltaYaw, deltaPitch;
    private boolean reachedYaw, reachedPitch, subCPS;

    @Override
    public void onDisable() {
        Slice.INSTANCE.target = null;
        target = null;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(target == null) target = getTarget();

        if(target == null)
            return;

        if (target.getHealth() <= 0 || target.isDead || (mc.thePlayer.isDead || mc.thePlayer.getHealth() <= 0)) {
            target = null;
            return;
        }

        mc.thePlayer.rotationYaw = getRotationsFixedSens(target)[0];
        mc.thePlayer.rotationPitch = getRotationsFixedSens(target)[1];

        Slice.INSTANCE.target = target;


        if(mc.thePlayer.getDistanceToEntity(target) <= rangeToPlayer.getValue().doubleValue()) {
            KeyUtil.moveKeys()[0].pressed = false;

            if(subCPS && (cps.getValue().intValue() < 2)) {
                subCPS = false;
            }

            if(timer.hasReached(1000L / (subCPS ? cps.getValue().intValue() - 1 : cps.getValue().intValue()))) {
                mc.clickMouse();
                timer.reset();

                if(cps.getValue().intValue() > 2) subCPS = !subCPS;
            }
            return;
        }
        KeyUtil.moveKeys()[0].pressed = true;
    }

    @SuppressWarnings("all")
    public EntityLivingBase getTarget() {
        double dist = 999.9D;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        Slice.INSTANCE.target = target;
        return target;
    }

    public float[] getRotationsFixedSens(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
                deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
                distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX - deltaZ)),
                pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if(deltaX < 0 && deltaZ < 0) yaw = (float) (90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        if(deltaX > 0 && deltaZ < 0) yaw = (float) (-90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        if(deltaX < 0 && deltaZ > 0) yaw = (float) (90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        if(deltaX > 0 && deltaZ > 0) yaw = (float) (-90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));

        if(deltaX == 0 && deltaZ < 0) yaw = -90;
        if(deltaX == 0 && deltaZ > 0) yaw = 90;
        if(deltaX < 0 && deltaZ == 0) yaw = 180;
        if(deltaX > 0 && deltaZ == 0) yaw = 0;

        if(pitch > 90) pitch = 90;
        if(pitch < -90) pitch = -90;

        return new float[]{yaw, pitch};
    }

}
