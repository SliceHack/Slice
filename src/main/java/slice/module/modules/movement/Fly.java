package slice.module.modules.movement;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
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
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla", "Astro");
    BooleanValue bobbing = new BooleanValue("Bobbing", true);
    NumberValue speed = new NumberValue("Speed", 3.0D, 0.1D, 6.0D, NumberValue.Type.DOUBLE);

    boolean up = false;


    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {

            EventUpdate e = (EventUpdate) event;

            if(mode.getValue().equalsIgnoreCase("Vanilla")) {
                mode.setValue("Astro");
            }

            // boobing
            if(bobbing.getValue() && MoveUtil.isMoving()) {
                mc.thePlayer.cameraPitch = 0.1F;
                mc.thePlayer.cameraYaw = 0.1F;
            }

            switch (mode.getValue()) {
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
                case "Astro":
                    mc.thePlayer.motionY = 0;
                    break;

            }

        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if(mc.theWorld == null)
                return;

            if(mode.getValue().equalsIgnoreCase("Astro")) {
                if(e.isIncomming())
                    return;

            }
        }
    }
}
