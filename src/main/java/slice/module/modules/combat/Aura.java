package slice.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

@ModuleInfo(name = "Aura", description = "Kills players around you!", key = Keyboard.KEY_R, category = Category.COMBAT)
@SuppressWarnings("all")
public class Aura extends Module {

    ModeValue blockMode = new ModeValue("Block Mode", "Vanilla", "Vanilla", "None", "NCP", "Fake");
    ModeValue rotateMode = new ModeValue("Rotation Mode", "Bypass", "Bypass", "Smooth", "None");

    NumberValue cps = new NumberValue("CPS", 8, 1, 20, NumberValue.Type.INTEGER);
    NumberValue range = new NumberValue("Range", 3.0, 0.2, 10.0, NumberValue.Type.DOUBLE);

    BooleanValue keepSprint = new BooleanValue("KeepSprint", true);
    BooleanValue noSwing = new BooleanValue("NoSwing", false);
    BooleanValue invis = new BooleanValue("Invisible", true);
    BooleanValue players = new BooleanValue("Players", true);
    BooleanValue mobs = new BooleanValue("Mobs", true);
    BooleanValue teams = new BooleanValue("Teams", false);

    EntityLivingBase target;

    public static boolean fakeBlock;

    /** for bypassing */
    private int deltaCps;
    private boolean reachedCps;

    /** smooth rotating */
    private float deltaYaw, deltaPitch;
    private boolean reachedYaw, reachedPitch, hasRotated;

    private float yaw, pitch;

    public void onDisable() {
        deltaPitch = 0;
        deltaYaw = 0;
        fakeBlock = false;
    }

    public void onEnable() {
        deltaPitch = mc.thePlayer.rotationPitchHead;
        deltaYaw = mc.thePlayer.rotationYawHead;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;

            if(target == null) {
                deltaYaw = mc.thePlayer.rotationYawHead;
                deltaPitch = mc.thePlayer.rotationPitchHead;
            }

            target = getTarget();

            if((target == null || target.isDead || target.getHealth() <= 0) && fakeBlock) {
                fakeBlock = false;
            }

            if(target == null) {
                reachedCps = false;
                reachedPitch = false;
                reachedYaw = false;
                deltaYaw = mc.thePlayer.rotationYawHead;
                deltaPitch = mc.thePlayer.rotationPitchHead;
                deltaCps = 0;
                hasRotated = false;
            }

            if(target != null) {
                if(!rotateMode.getValue().equalsIgnoreCase("None")) {
                    e.setYaw(yaw);
                    e.setPitch(pitch);
                }

                switch (rotateMode.getValue()) {
                    case "Bypass":
                        yaw = getBypassRotate(target)[0];
                        pitch = getBypassRotate(target)[1];
                        break;
                    case "Smooth":
                        yaw = getRotationsFixedSens(target)[0];
                        pitch = getRotationsFixedSens(target)[1];
                        break;
                    case "None":
                        yaw = mc.thePlayer.rotationYaw;
                        pitch = mc.thePlayer.rotationPitch;
                        break;
                    default:
                        yaw = getRotate(target)[0];
                        pitch = getRotate(target)[1];
                        break;
                }


                boolean block = mc.thePlayer.getHeldItem() != null && !blockMode.getValue().equalsIgnoreCase("None");

                fakeBlock = block && (blockMode.getValue().equalsIgnoreCase("Fake") || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword));

                if ((block && !fakeBlock) && blockMode.getValue().equalsIgnoreCase("Vanilla") && e.isPre()) {
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                }

                if((block && !fakeBlock) && blockMode.getValue().equalsIgnoreCase("NCP")) {
                    if(e.isPre()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }
                }

                deltaCps = deltaCps == cps.getValue().intValue() ? (cps.getValue().intValue() != 1 ? (cps.getValue().intValue() - 1) : 1) : cps.getValue().intValue();

                if(e.isPre()) {
                    if (timer.hasReached(1000 / deltaCps)) {
                        attack();
                        if (!noSwing.getValue()) mc.thePlayer.swingItem();
                        timer.reset();
                    }
                }
            }

        }
    }

    private void attack() {
        if (keepSprint.getValue()) mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        else mc.playerController.attackEntity(mc.thePlayer, target);
    }

    public float[] getBypassRotate(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX;
        double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX - deltaZ));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if(deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        }
        if(deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        }
        return new float[]{yaw, pitch};
    }

    public float[] getRotate(Entity e) {
        double x = e.posX - mc.thePlayer.posX;
        double y = e.posY - mc.thePlayer.posY;
        double z = e.posZ - mc.thePlayer.posZ;
        double dist = Math.sqrt(x * x + y * y + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);
        reachedYaw = true;
        reachedPitch = true;
        return new float[] {yaw, pitch};
    }

    @SuppressWarnings("all")
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

    public float[] getRotationsFixedSens(Entity e) {
        try {
            double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX;
            double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
            double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ;
            double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

            float yaw = (float) Math.toDegrees(-Math.atan(deltaX - deltaZ));
            float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

            if(deltaX < 0 && deltaZ < 0) {
                yaw = (float) (90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
            }
            if(deltaX > 0 && deltaZ < 0) {
                yaw = (float) (-90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
            }

            if ((int)pitch != deltaPitch) reachedPitch = false;
            else if ((int)yaw != deltaYaw) reachedYaw = false;
            else if ((int)pitch == deltaPitch) reachedPitch = true;
            else if ((int)yaw == deltaYaw) reachedYaw = true;

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
                    deltaYaw += Math.abs(yaw - deltaYaw) / 3;
                } else {
                    deltaYaw -= Math.abs(yaw - deltaYaw) / 3;
                }
            }
            if (deltaPitch > 90) deltaPitch = 90;
            else if (deltaPitch < -90) deltaPitch = -90;
        } catch (Exception ignored){}

        return new float[] {deltaYaw, deltaPitch};
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
        return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= reach && !(entity instanceof EntityArmorStand && entity.isInvisible());
    }

}
