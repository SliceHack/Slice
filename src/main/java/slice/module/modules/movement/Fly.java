package slice.module.modules.movement;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventClientTick;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;
import slice.util.MoveUtil;
import slice.util.RotationUtil;

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
@SuppressWarnings("all")
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "Dev", "PvPGym", "Zonecraft", "Vulcan", "Vulcan2");
    BooleanValue bobbing = new BooleanValue("Bobbing", true);
    NumberValue speed = new NumberValue("Speed", 3.0D, 0.1D, 6.0D, NumberValue.Type.DOUBLE);

    boolean up = false;
    private int stage;

    private int ticks;

    private int posY;

    private int currentSlot;

    private ItemStack bow;

    private int i;

    public void onEnable() {
        stage = 0;
        switch (mode.getValue()) {
            case "Vulcan":
                LoggerUtil.addMessage("You must have a bow in your hotbar");
                break;
            case "UwUGuard":
                if(!mc.thePlayer.onGround) break;
                MoveUtil.jump();
                break;
            case "PixelEdge":
                for(double i = 0; i < 3.52F; i += 0.1F) {
                    mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ, false));
                }
                mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer(true));
                break;

        }
    }

    public void onDisable() {
        ticks = 0;
        stage = 0;
        mc.timer.timerSpeed = 1.0F;
        posY = (int) mc.thePlayer.posY;
        mc.thePlayer.jumpMovementFactor = 0.02F;
        mc.thePlayer.speedInAir = 0.02F;
    }

    public void onUpdateNoToggle(EventUpdate event) {
        speed.setHidden(!mode.getValue().equalsIgnoreCase("Vanilla"));
    }

    @EventInfo
    public void onTick(EventClientTick e) {
        if(mode.getValue().equalsIgnoreCase("Vulcan")) {
            if (currentSlot != i) {
                ticks++;
            }
        }
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        // boobing
        if(bobbing.getValue() && MoveUtil.isMoving()) {
            mc.thePlayer.cameraPitch = 0.1F;
            mc.thePlayer.cameraYaw = 0.1F;
        }

        switch (mode.getValue()) {
            case "Vulcan2":
                if(stage <= 1) {
                    MoveUtil.stop();
                }

                if(stage == 0) {
                    damage(3.14F);
                    stage = 1;
                }

                if(stage == 1 && mc.thePlayer.hurtResistantTime > 12) {
                    stage = 2;
                }

                if(stage == 2) {
                    mc.thePlayer.motionY = 0F;
                    mc.timer.timerSpeed = 2F;
                }
                break;
            case "Vulcan":
                if(stage < 2) {
                    stage = 3;
                }

                if(stage == 2 && mc.thePlayer.hurtResistantTime <= 0) {
                    stage = 3;
                }

                if(stage == 3) {
                    if (mc.thePlayer.ticksExisted % 5 == 0) mc.thePlayer.motionY = -0.1F;
                }
                break;
            case "Vanilla":
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = speed.getValue().doubleValue();
                } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -speed.getValue().doubleValue();
                } else {
                    mc.thePlayer.motionY = up ? 0 : 0.001;
                    up = !up;
                }
                MoveUtil.strafe(speed.getValue().doubleValue());
                break;
            // wip
            case "UwUGuard":
                if(mc.thePlayer.ticksExisted % 10 == 0) {
                    float yaw = mc.thePlayer.rotationYaw;

                    double x = mc.thePlayer.posX + Math.cos(Math.toRadians(yaw + 90));
                    double z = mc.thePlayer.posZ + Math.sin(Math.toRadians(yaw + 90));
                    double y = mc.thePlayer.posY;
                    mc.thePlayer.setPosition(x, y, z);
                }
                break;
            case "PvPGym":
                if(stage < 2) {
                    MoveUtil.stop();
                }

                if(stage == 0) {
                    damage(3.4F);
                    stage = 1;
                }

                if(stage == 1 && mc.thePlayer.hurtResistantTime > 6) {
                    stage = 2;
                }

                if(stage == 2) {
                    mc.thePlayer.motionY = 0F;
                }
                break;
            case "UwUGuardGlide":
                if(mc.thePlayer.onGround) return;
                MoveUtil.strafe(7);
                mc.timer.timerSpeed = 0.1f;
                break;
            case "Zonecraft":
                if(!MoveUtil.isMoving()) MoveUtil.resetMotion(false);
                if(mc.thePlayer.onGround) { MoveUtil.jump(); break; }

                mc.thePlayer.motionY = 0.0;

                if(mc.thePlayer.ticksExisted % 20 < 10) mc.timer.timerSpeed = 1.25f;
                else mc.timer.timerSpeed = 0.8f;

                if(mc.thePlayer.ticksExisted % 20 == 9) MoveUtil.strafe(MoveUtil.getSpeed() * 1.125f);
                if(mc.thePlayer.ticksExisted % 20 == 1) MoveUtil.strafe((float)(0.2783*1.2));
                break;
        }
    }

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if(mc.theWorld == null)
            return;

        if(mode.getValue().equalsIgnoreCase("Vulcan")) {
            if(stage == 2) {
                if(e.isOutgoing() && !(p instanceof C03PacketPlayer)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public void useBow() {
        int i2;
        for (i2 = 0; i2 < 9; ++i2) {
            i = i2;
            bow = mc.thePlayer.inventoryContainer.getSlot(i2 + 36).getStack();
            if (bow != null) {
                final Item item = bow.getItem();
                if (item instanceof ItemBow) {
                    for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                        final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
                        if (stack != null) {
                            if (stack.getItem().getUnlocalizedName().contains("arrow")) {

                                if (currentSlot != i2) {
                                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i2));
                                    mc.thePlayer.inventory.currentItem = i2;
                                }
                                currentSlot = i2;
                            }
                        }
                    }
                }
            }

        }
    }
}
