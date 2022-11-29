package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import slice.event.data.EventInfo;
import slice.event.events.EventAttack;
import slice.event.events.EventClientTick;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.util.KeyUtil;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;
import slice.util.PacketUtil;

@ModuleInfo(name = "Speed", description = "Allows you to move fast!!", key = Keyboard.KEY_X, category = Category.MOVEMENT)
public class Speed extends Module {

    ModeValue mode = new ModeValue("Mode", "Bhop", "Bhop", "Hycraft", "Dev", "Zonecraft", "Astro", "MMC", "UwUGuard", "Legit", "Dac");
    private boolean wait;
    private int onGroundTicks, offGroundTicks, waitTicks, ticks;

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        onGroundTicks = 0;
        offGroundTicks = 0;
        mc.thePlayer.speedInAir = 0.02F;
        mc.thePlayer.jumpMovementFactor = 0.02F;
        KeyUtil.moveKeys()[0].pressed = false;
        KeyUtil.moveKeys()[2].pressed = false;
        KeyUtil.moveKeys()[4].pressed = false;
        mc.timer.timerSpeed = 1f;
        mc.gameSettings.keyBindJump.pressed = mc.gameSettings.keyBindJump.isKeyDown();
    }

    public void onEnable() {
        if(mode.getValue().equalsIgnoreCase("UwUGuard")) LoggerUtil.addMessage("Legacy Bypass may no longer work");
    }

    @EventInfo
    public void onTick(EventClientTick e) {
        if(mc.thePlayer.onGround) {
            offGroundTicks = 0;
            onGroundTicks++;
        } else {
            onGroundTicks = 0;
            offGroundTicks++;
        }
        if(wait) {
            ticks++;
            if(ticks >= waitTicks) ticks = 0; waitTicks = 0;
        }
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(!mode.getValue().equalsIgnoreCase("Matrix")) {
            mc.thePlayer.speedInAir = 0.02F;
            mc.thePlayer.jumpMovementFactor = 0.02F;
        }

        switch (mode.getValue()) {
            case "Zonecraft":
                if(!MoveUtil.isMoving()) break;

                mc.gameSettings.keyBindJump.pressed = false;

                if(mc.thePlayer.onGround) mc.thePlayer.motionY = 0.0784;
                break;
            case "Bhop":
                if(!MoveUtil.isMoving()) break;

                if (mc.thePlayer.onGround) {
                    MoveUtil.jump();
                }
                MoveUtil.strafe((MoveUtil.getSpeed())+0.02);
                break;
            case "MMC":
                if(!MoveUtil.isMoving()) break;

                if(mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.56F;
                    MoveUtil.strafe(0.48D);
                }
                break;
            case "Astro":
                if(mc.thePlayer.fallDistance > 4)
                    break;

                if(!MoveUtil.isMoving()) break;

                if(mc.thePlayer.onGround) {
                    MoveUtil.jump();
                    MoveUtil.strafe(0.48);
                }

                if(offGroundTicks >= 7) {
                    mc.thePlayer.motionY = -2F;
                }
                break;
            case "UwUGuard":
                if(((mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isCollidedVertically) || !(mc.thePlayer.fallDistance <= 1)) || !MoveUtil.isMoving()) break;

                if(mc.thePlayer.onGround) { MoveUtil.jump(); MoveUtil.strafe(0.42); }
                if(offGroundTicks > 5) { mc.thePlayer.motionY = -2F; }
                break;
            case "Legit":
                mc.thePlayer.setSprinting(true);
                KeyUtil.moveKeys()[0].pressed = true;
                KeyUtil.moveKeys()[4].pressed = true;

                int setYaw = 0;
                int direction = MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
                switch(direction) {
                    case 0:
                        setYaw = 0;
                        break;
                    case 1:
                        setYaw = 90;
                        break;
                    case 2:
                        setYaw = 180;
                        break;
                    case 3:
                        setYaw = -90;
                        break;
                }

                if(mc.thePlayer.onGround) {
                    mc.thePlayer.rotationYaw = setYaw;
                    KeyUtil.moveKeys()[2].pressed = false;
                    break;
                }

                if (mc.thePlayer.lastReportedYaw == setYaw) {
                    mc.thePlayer.rotationYaw = setYaw + 44.99F;
                    KeyUtil.moveKeys()[2].pressed = true;
                }
                break;
            case "Dac":
                double speed = 0.33;
                if(!MoveUtil.isMoving()) break;
                if(mc.thePlayer.moveForward < 0) speed -= 0.1;
                if(mc.thePlayer.onGround) MoveUtil.jump();
                else speed += 0.04;
                if(mc.thePlayer.fallDistance <= 3 && !mc.thePlayer.onGround) mc.thePlayer.motionY = -0.1;

                MoveUtil.strafe(speed);
                break;
        }
    }

    @EventInfo
    public void onAttack(EventAttack e) {
        wait = true;
        waitTicks = 5;
    }

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if(mc.theWorld == null) return;

        switch (mode.getValue()) {
            case "UwUGuard":
                if(e.isIncomming()) break;

                if(p instanceof C03PacketPlayer) {
                    C03PacketPlayer c03 = (C03PacketPlayer) p;
                    if(c03.isMoving()) {
                        c03.setMoving(false);
                        e.setCancelled(true);
                        mc.thePlayer.sendQueue.addToSendQueue(c03);
                    }
                }
                break;
            case "Hycraft":
                if(p instanceof S18PacketEntityTeleport) {
                    e.setCancelled(true);
                }
                break;
            case "Dac":
//                if(timer.hasReached(500L)) timer.reset();
//                e.setCancelled(PacketUtil.isMovementPacket(p) && !timer.hasTimeReached(500L));
                break;
        }
    }


}
