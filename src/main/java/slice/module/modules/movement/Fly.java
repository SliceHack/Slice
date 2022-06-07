package slice.module.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import slice.event.Event;
import slice.event.events.EventPacket;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.MoveUtil;

@ModuleInfo(name = "Fly", key = Keyboard.KEY_G, description = "Allows you to fly like a bird", category = Category.MOVEMENT)
public class Fly extends Module {

    ModeValue mode = new ModeValue("Mode", "Vanilla", "Vanilla");
    NumberValue speed = new NumberValue("Speed", 3.0D, 0.1D, 6.0D, NumberValue.Type.DOUBLE);

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            switch (mode.getValue()) {
                case "Vanilla":
                    if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY = speed.getValue().doubleValue();
                    } else if(mc.gameSettings.keyBindSprint.isKeyDown()) {
                        mc.thePlayer.motionY = -speed.getValue().doubleValue();
                    } else {
                        mc.thePlayer.motionY = 0;
                    }
                    MoveUtil.strafe(speed.getValue().doubleValue());
                    break;

            }

        }
        if(event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
        }
    }
}
