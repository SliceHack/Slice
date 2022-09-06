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
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.event.Event;
import slice.event.data.EventInfo;
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

    ModeValue mode = new ModeValue("Mode", "Switch", "Switch", "Lock");
    ModeValue blockMode = new ModeValue("Block Mode", "Vanilla", "Vanilla", "None", "NCP", "Fake");
    ModeValue rotateMode = new ModeValue("Rotation Mode", "Bypass", "Bypass", "Smooth", "None");

    NumberValue cps = new NumberValue("CPS", 8, 1, 20, NumberValue.Type.INTEGER);
    NumberValue range = new NumberValue("Range", 3.0, 0.2, 10.0, NumberValue.Type.DOUBLE);
    NumberValue rotateRange = new NumberValue("Rotate Range", 5.0, 0.2, 20.0, NumberValue.Type.DOUBLE);

    BooleanValue delay9 = new BooleanValue("1.9 Delay", false);
    BooleanValue throughWalls = new BooleanValue("Through Walls", false);

    BooleanValue keepSprint = new BooleanValue("KeepSprint", true);
    BooleanValue noSwing = new BooleanValue("NoSwing", false);
    BooleanValue invis = new BooleanValue("Invisible", true);
    BooleanValue players = new BooleanValue("Players", true);
    BooleanValue mobs = new BooleanValue("Mobs", true);
    BooleanValue teams = new BooleanValue("Teams", false);

    EntityLivingBase target, rotateTarget;

    public static boolean fakeBlock;

    /** for bypassing */
    private int deltaCps;
    private boolean reachedCps;

    /** smooth rotating */
    private float deltaYaw, deltaPitch;
    private boolean reachedYaw, reachedPitch, hasRotated;

    private float yaw, pitch;
    private boolean ran;


    public void onUpdateNoToggle(EventUpdate event) {
        if(isEnabled()) return;

        deltaPitch = mc.thePlayer.rotationPitch;
        deltaYaw = mc.thePlayer.rotationYaw;
        hasRotated = false;
    }

    public void onDisable() {
        ran = false;
        fakeBlock = false;
        Slice.INSTANCE.target = null;
        rotateTarget = null;
    }

    public void onEnable() {
        deltaPitch = mc.thePlayer.rotationYaw;
        deltaYaw = mc.thePlayer.rotationPitch;
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {

        if (rotateTarget == null) {
            deltaYaw = mc.thePlayer.rotationYaw;
            deltaPitch = mc.thePlayer.rotationPitch;
        }
        if(rotateTarget != null && !mode.getValue().equalsIgnoreCase("None")) {
            e.setYaw(yaw);
            e.setPitch(pitch);
        }

        if (rotateTarget != null) {
            float yaw, pitch;
            switch (rotateMode.getValue()) {
                case "Bypass":
                    yaw = getBypassRotate(rotateTarget)[0];
                    pitch = getBypassRotate(rotateTarget)[1];
                    break;
                case "Smooth":
                    yaw = getRotationsFixedSens(rotateTarget)[0];
                    pitch = getRotationsFixedSens(rotateTarget)[1];
                    break;
                case "None":
                    yaw = mc.thePlayer.rotationYaw;
                    pitch = mc.thePlayer.rotationPitch;
                default:
                    yaw = getRotationsFixedSens(rotateTarget)[0];
                    pitch = getRotationsFixedSens(rotateTarget)[1];
                    break;
            }

            if(yaw != mc.thePlayer.rotationYaw && pitch != mc.thePlayer.rotationPitch && yaw != this.yaw && pitch != this.pitch) {
                this.yaw = yaw;
                this.pitch = pitch;
            }

            if (!rotateMode.getValue().equalsIgnoreCase("Smooth")) {
                hasRotated = true;
            }
        }

        try {

            switch (mode.getValue()) {
                case "Switch":
                    target = swapAura(target, range.getValue().doubleValue());
                    break;
                case "Lock":
                    target = getTarget();
                    break;
            }

            if ((target == null || target.isDead || target.getHealth() <= 0) && fakeBlock) {
                fakeBlock = false;
            }

            if(!throughWalls.getValue() && !mc.thePlayer.canEntityBeSeen(target)) {
                deltaPitch = mc.thePlayer.rotationYaw;
                deltaYaw = mc.thePlayer.rotationYaw;
                fakeBlock = false;
                Slice.INSTANCE.target = null;
                rotateTarget = null;
                return;
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
                        cps -= 0.1;
                    }

                }

                if (e.isPre()) {
                    if (timer.hasReached((long) (1000 / cps)) && hasReached(target)) {

                        if (!noSwing.getValue()) mc.thePlayer.swingItem();
                        timer.reset();
                        attack();

                    }
                }
            }
        } catch(Exception ignored){}
        switch (mode.getValue()) {
            case "Switch":
                rotateTarget = swapAuraRotate(rotateTarget, rotateRange.getValue().doubleValue());
                break;
            case "Lock":
                rotateTarget = getRotateTarget();
                break;
        }

        if(rotateTarget == null) {
            hasRotated = false;
        }
    }

    public boolean hasReached(EntityLivingBase target) {
        return  mode.getValue().equalsIgnoreCase("Smooth") ? ran : true;
    }

    public boolean isRoughlyEqual(float a, float b) {
        return Math.abs(a - b) < 2.2;
    }

    public EntityLivingBase swapAura(EntityLivingBase target, double ranage) {
        if(target != null) {
            if(target.getDistanceToEntity(mc.thePlayer) > ranage || (target.isDead || target.getHealth() <= 0)) {
                return target = null;
            }
        }
        target = getTarget();

        return target;
    }

    public EntityLivingBase swapAuraRotate(EntityLivingBase target, double ranage) {
        if(target != null) {
            if(target.getDistanceToEntity(mc.thePlayer) > ranage || (target.isDead || target.getHealth() <= 0)) {
                return target = null;
            }
        }
        target = getRotateTarget();

        return target;
    }


    private void attack() {
        if (keepSprint.getValue()) mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        else mc.playerController.attackEntity(mc.thePlayer, target);
    }

    public float[] getBypassRotate(Entity e) {
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

    @SuppressWarnings("all")
    public EntityLivingBase getRotateTarget() {
        double dist = rotateRange.getValue().doubleValue();
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
        rotateTarget = target;
        Slice.INSTANCE.target = target;
        return target;
    }

    public float[] getRotationsFixedSens(Entity e) {
        float yaw = getBypassRotate(e)[0];
        float pitch = getBypassRotate(e)[1];

        float sens = 9F;
        float newYaw = MathHelper.wrapAngleTo180_float(deltaYaw - yaw);
        float newPitch = MathHelper.wrapAngleTo180_float(deltaPitch - pitch);
        if (newYaw > sens) newYaw = sens;
        if (newYaw < -sens) newYaw = -sens;
        if (newPitch > sens) newPitch = sens;
        if (newPitch < -sens) newPitch = -sens;
        if (deltaPitch > 90) deltaPitch = 90;

        deltaYaw -= newYaw;
        deltaPitch -= newPitch;
        hasRotated = Math.abs(newYaw) < 0.1 && Math.abs(newPitch) < 0.1;

        return new float[] { deltaYaw, deltaPitch };
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
        return entity != mc.thePlayer && entity.isEntityAlive() && !(entity instanceof EntityArmorStand && entity.isInvisible());
    }

}
