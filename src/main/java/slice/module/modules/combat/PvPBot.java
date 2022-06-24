package slice.module.modules.combat;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;
import slice.util.KeyUtil;

@ModuleInfo(name = "PvPBot", category = Category.COMBAT)
@Getter
public class PvPBot extends Module {

    NumberValue rangeToPlayer = new NumberValue("Range To Stop", 3.14D, 1.0D, 3.14D, NumberValue.Type.DOUBLE);
    NumberValue cps = new NumberValue("CPS", 10, 1, 20, NumberValue.Type.INTEGER);

    private EntityLivingBase target;

    private float deltaYaw, deltaPitch;
    private boolean reachedYaw, reachedPitch, subCPS;

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(target == null) target = getTarget();

            if(target == null)
                return;

            if (target.getHealth() <= 0 || target.isDead || (mc.thePlayer.isDead || mc.thePlayer.getHealth() <= 0)) {
                target = null;
                return;
            }

            mc.thePlayer.rotationYaw = getRotationsFixedSens(target)[0];
            mc.thePlayer.rotationPitch = getRotationsFixedSens(target)[1];


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
        return target;
    }

    public float[] getRotationsFixedSens(Entity e) {
        try {
            double x = e.posX - mc.thePlayer.posX;
            double y = e.posY - mc.thePlayer.posY;
            double z = e.posZ - mc.thePlayer.posZ;

            double dist = Math.sqrt(x * x + y * y + z * z);
            float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);

            if (pitch != deltaPitch) reachedPitch = false;
            else if (yaw != deltaYaw) reachedYaw = false;
            else if (pitch == deltaPitch) reachedPitch = true;
            else if (yaw == deltaYaw) reachedYaw = true;

            int smooth = 2;
            if (!reachedPitch) {
                if (pitch > deltaPitch) {
                    deltaPitch += Math.abs(pitch - deltaPitch) / smooth;
                } else {
                    deltaPitch -= Math.abs(pitch - deltaPitch) / smooth;
                }
            }
            if (!reachedYaw) {
                if (yaw > deltaYaw) {
                    deltaYaw += Math.abs(yaw - deltaYaw) / smooth;
                } else {
                    deltaYaw -= Math.abs(yaw - deltaYaw) / smooth;
                }
            }
            if (deltaPitch > 90) deltaPitch = 90;
            else if (deltaPitch < -90) deltaPitch = -90;
        } catch (Exception ignored){}

        return new float[] {deltaYaw, deltaPitch+(float)(Math.random()-0.02)};
    }

}
