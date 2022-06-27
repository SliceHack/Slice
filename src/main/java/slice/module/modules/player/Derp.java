package slice.module.modules.player;

import java.util.concurrent.ThreadLocalRandom;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;
import slice.util.LoggerUtil;

@ModuleInfo(name = "Derp", description = "Makes you spin around", category = Category.PLAYER)
public class Derp extends Module {

    ModeValue mode = new ModeValue("Mode", "Random", "Random", "Spin");

    BooleanValue yaw = new BooleanValue("Yaw", true);
    BooleanValue pitch = new BooleanValue("Pitch", false);

    NumberValue yawSpeed = new NumberValue("Yaw Speed", 1.0F,  0.1F, 10.0F, NumberValue.Type.FLOAT);
    NumberValue pitchSpeed = new NumberValue("Pitch Speed", 1.0F,  0.1F, 10.0F, NumberValue.Type.FLOAT);

    BooleanValue throughHead = new BooleanValue("Through Head", false);

    private float spinYaw, spinPitch;

    public void onEnable() {
        spinYaw = mc.thePlayer.rotationYaw;
    }

    public void onUpdate(EventUpdate event) {
        yaw.setHidden(!mode.getValue().equals("Spin"));
        pitch.setHidden(!mode.getValue().equals("Spin"));

        if(mode.getValue().equalsIgnoreCase("Spin")) {

            yawSpeed.setHidden(!yaw.getValue());

            pitchSpeed.setHidden(!pitch.getValue());
            throughHead.setHidden(!pitch.getValue());

        }

    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            switch (mode.getValue()) {
                case "Random":
                    int randomYaw = ThreadLocalRandom.current().nextInt(0, 360 + 1);
                    int randomPitch = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
                    e.setYaw(randomYaw);
                    e.setPitch(randomPitch);
                    break;
                case "Spin":
                    if(yaw.getValue()) {
                        e.setYaw(spinYaw);

                        if (spinYaw >= 360) spinYaw = 0;
                        else spinYaw += yawSpeed.getValue().floatValue();
                    }
                    if(pitch.getValue()) {
                        e.setPitch(spinPitch);

                        float value = throughHead.getValue() ? 360 : 90;
                        if(spinPitch >= value) spinPitch = 0;
                        else spinPitch += pitchSpeed.getValue().floatValue();
                    }
                    break;
            }
        }
    }
}
