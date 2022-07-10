package slice.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import slice.Slice;
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

    BooleanValue delay9 = new BooleanValue("1.9 Delay", false);

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
        Slice.INSTANCE.target = null;
    }

    public void onEnable() {
        deltaPitch = mc.thePlayer.rotationPitchHead;
        deltaYaw = mc.thePlayer.rotationYawHead;
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            try {
                EventUpdate e = (EventUpdate) event;

                if (target == null) {
                    deltaYaw = mc.thePlayer.rotationYawHead;
                    deltaPitch = mc.thePlayer.rotationPitchHead;
                }

                target = getTarget();

                if ((target == null || target.isDead || target.getHealth() <= 0) && fakeBlock) {
                    fakeBlock = false;
                }

                if (target == null) {
                    reachedCps = false;
                    reachedPitch = false;
                    reachedYaw = false;
                    deltaYaw = mc.thePlayer.rotationYawHead;
                    deltaPitch = mc.thePlayer.rotationPitchHead;
                    deltaCps = 0;
                    hasRotated = false;
                }

                if (target != null) {
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
                            yaw = getRotationsFixedSens(target)[0];
                            pitch = getRotationsFixedSens(target)[1];
                            break;
                    }

                    if (!rotateMode.getValue().equalsIgnoreCase("None")) {
                        e.setYaw(yaw);
                        e.setPitch(pitch);
                    }


                    boolean block = mc.thePlayer.getHeldItem() != null && !blockMode.getValue().equalsIgnoreCase("None");

                    fakeBlock = block && (blockMode.getValue().equalsIgnoreCase("Fake") || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword));

                    if ((block && !fakeBlock) && blockMode.getValue().equalsIgnoreCase("Vanilla") && e.isPre()) {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }

                    if ((block && !fakeBlock) && blockMode.getValue().equalsIgnoreCase("NCP")) {
                        if (e.isPre()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        } else {
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                        }
                    }

                    deltaCps = deltaCps == cps.getValue().intValue() ? (cps.getValue().intValue() != 1 ? (cps.getValue().intValue() - 1) : 1) : cps.getValue().intValue();

                    double cps = this.cps.getValue().intValue();
                    if (delay9.getValue()) {
                        cps = 4;

                        if (mc.thePlayer.getHeldItem() != null) {
                            final Item item = mc.thePlayer.getHeldItem().getItem();

                            if (item instanceof ItemSpade || item == Items.golden_axe || item == Items.diamond_axe || item == Items.wooden_hoe || item == Items.golden_hoe)
                                cps = 1;
                            else if (item == Items.wooden_axe || item == Items.stone_axe) cps = 0.8;
                            else if (item instanceof ItemSword) cps = 1.6;
                            else if (item instanceof ItemPickaxe) cps = 1.2;
                            else if (item == Items.iron_axe) cps = 0.9;
                            else if (item == Items.stone_hoe) cps = 2;
                            else if (item == Items.iron_hoe) cps = 3;
                            cps += 0.9;
                        }

                    }

                    if (e.isPre()) {
                        if (timer.hasReached((long) (1000 / cps))) {
                            attack();
                            if (!noSwing.getValue()) mc.thePlayer.swingItem();
                            timer.reset();
                        }
                    }
                }
            } catch(Exception ignored){}
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

        if(deltaX < 0 && deltaZ < 0) yaw = (float) (90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));
        else if(deltaX > 0 && deltaZ < 0) yaw = (float) (-90 + (Math.toDegrees(Math.atan(deltaZ / deltaX))));

        if(pitch > 90) pitch = 90;
        else if(pitch < -90) pitch = -90;

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
        Slice.INSTANCE.target = target;
        return target;
    }

    public float[] getRotationsFixedSens(Entity e) {
        float yaw = getBypassRotate(e)[0];
        float pitch = getBypassRotate(e)[1];

        try {
            int smooth = 2;

            if (deltaPitch < pitch) deltaPitch += Math.abs(pitch - deltaPitch) / smooth;
            else deltaPitch -= Math.abs(pitch - deltaPitch) / smooth;

            if (deltaYaw < yaw) deltaYaw += Math.abs(yaw - deltaYaw) / 3;
            else deltaYaw -= Math.abs(yaw - deltaYaw) / 3;

            if(deltaPitch > 90) deltaPitch = 90;
            else if(deltaPitch < -90) deltaPitch = -90;
        } catch (Exception ignored){}

        return new float[] { deltaYaw, deltaPitch};
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
