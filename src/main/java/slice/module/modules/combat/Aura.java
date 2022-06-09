package slice.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Aura", description = "Kills players around you!", category = Category.COMBAT)
public class Aura extends Module {

    NumberValue cps = new NumberValue("CPS", 8, 1, 20, NumberValue.Type.INTEGER);
    NumberValue range = new NumberValue("Range", 3.0, 0.2, 10.0, NumberValue.Type.DOUBLE);

    BooleanValue keepSprint = new BooleanValue("KeepSprint", true);

    BooleanValue noSwing = new BooleanValue("NoSwing", false);
    BooleanValue invis = new BooleanValue("Invisible", true);
    BooleanValue players = new BooleanValue("Players", true);
    BooleanValue mobs = new BooleanValue("Mobs", true);
    BooleanValue teams = new BooleanValue("Teams", false);

    EntityLivingBase target;

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            target = getTarget();

            if(target == null)
                return;

            attack();
            e.setYaw(getRotate(target)[0]);
            e.setPitch(getRotate(target)[1]);
        }
    }

    public void attack() {
        if(timer.hasReached(1000 / cps.getValue().intValue())) {
            if(keepSprint.getValue()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                return;
            }
            mc.playerController.attackEntity(mc.thePlayer, target);
        }
    }

    public float[] getRotate(Entity e) {
        double x = e.posX - mc.thePlayer.posX;
        double y = e.posY - mc.thePlayer.posY;
        double z = e.posZ - mc.thePlayer.posZ;
        double dist = Math.sqrt(x * x + y * y + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);

        if(pitch < -90.0F) pitch = -90.0F;
        else if(pitch > 90.0F) pitch = 90.0F;

        return new float[]{yaw, pitch};
    }

    public EntityLivingBase getTarget() {
        double dist = range.getValue().doubleValue();
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
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

    public boolean canAttack(EntityLivingBase entity) {
        boolean player = players.getValue();
        boolean invis = this.invis.getValue();
        boolean animal = mobs.getValue();
        boolean monster = mobs.getValue();
        boolean villager = mobs.getValue();
        boolean team = teams.getValue();

        float reach = (float) range.getValue().doubleValue();

        if(entity instanceof EntityPlayer || entity instanceof EntityAnimal || entity instanceof EntityMob || entity instanceof EntityVillager) {
            if(entity instanceof EntityPlayer && !player) {
                return false;
            }
            if(entity instanceof EntityAnimal && !animal) {
                return false;
            }
            if(entity instanceof EntityMob && !monster) {
                return false;
            }
            if(entity instanceof EntityVillager && !villager) {
                return false;
            }
        }
        if(entity.isOnSameTeam(mc.thePlayer) && team) {
            return false;
        }
        if(entity.isInvisible() && !invis) {
            return false;
        }
        return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= reach;
    }

}
