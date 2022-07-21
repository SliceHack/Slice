package slice.module.modules.movement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import slice.event.data.EventInfo;
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

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
@SuppressWarnings("all")
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "Dev", "PvPGym", "Hycraft", "UwUGuard", "Vulcan", "Vulcan2", "UwUGuardGlide");
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
        if(mode.getValue().equalsIgnoreCase("Vulcan")) {
            LoggerUtil.addMessage("You must have a bow in your hotbar");
        }
        if(mode.getValue().equalsIgnoreCase("UwUGuard") && mc.thePlayer.onGround) {
            MoveUtil.jump();
        }
        if(mode.getValue().equalsIgnoreCase("PixelEdge")) {
            for(double i = 0; i < 3.52F; i += 0.1F) {
                mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ, false));
            }
            mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendNoEvent(new C03PacketPlayer(true));
        }
    }

    public void onDisable() {
        ticks = 0;
        stage = 0;
        mc.timer.timerSpeed = 1.0F;
        posY = (int) mc.thePlayer.posY;
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

                if(stage == 0) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, -90, mc.thePlayer.onGround));
                    useBow();

                    if(mc.thePlayer.hurtResistantTime > 3) {
                        stage = 1;
                    }
                    MoveUtil.stop();
                }


                if(stage == 1 && mc.thePlayer.hurtResistantTime > 12 && mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 2F;
                    stage = 2;
                }

                if(stage == 2 && mc.thePlayer.hurtResistantTime <= 0) {
                    stage = 3;
                }

                if(stage == 3) {
                    if (mc.thePlayer.ticksExisted % 5 == 0) mc.thePlayer.motionY = -0.1F;

                    if(mc.thePlayer.onGround) {
                        onDisable();
                        setEnabled(false);
                    }
                }
                break;
            case "Vanilla":
                if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = speed.getValue().doubleValue();
                } else if(mc.gameSettings.keyBindSprint.isKeyDown()) {
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
            case "Hycraft":
                mc.thePlayer.motionY = 0F;
                mc.timer.timerSpeed = 10.0F;
                break;
            case "Dev":
                mc.thePlayer.motionY = 0F;
                break;
        }
    }

    @EventInfo
    public void onPacket(EventPacket e) {
        Packet<?> p = e.getPacket();

        if(mc.theWorld == null)
            return;

        if(mode.getValue().equalsIgnoreCase("Hycraft")) {
            if (p instanceof C03PacketPlayer.C06PacketPlayerPosLook
                    || p instanceof C03PacketPlayer.C04PacketPlayerPosition
                    || p instanceof C03PacketPlayer.C05PacketPlayerLook) {

                e.setCancelled(mc.thePlayer.ticksExisted % 5 != 0);
            }
        }
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
