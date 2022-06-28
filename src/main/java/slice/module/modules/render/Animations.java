package slice.module.modules.render;

import lombok.Getter;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.BooleanValue;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Animations", description = "Block Hit Animation", category = Category.RENDER)
@Getter
public class Animations extends Module {

    ModeValue mode = new ModeValue("Mode", "1.7", "1.7", "Spin", "Push", "Custom");

    BooleanValue restore = new BooleanValue("Restore", false);
    BooleanValue spin = new BooleanValue("Spin", false);

    NumberValue spinAngle = new NumberValue("Spin Angle", 360, 0, 360, NumberValue.Type.INTEGER);

    NumberValue push = new NumberValue("Push", 12.0F, 1.0F, 150.0F, NumberValue.Type.FLOAT);

    NumberValue x1 = new NumberValue("X 1", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue y1 = new NumberValue("Y 1", 1.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue z1 = new NumberValue("Z 1", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue x2 = new NumberValue("X 2", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue y2 = new NumberValue("Y 2", 1.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue z2 = new NumberValue("Z 2", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue x3 = new NumberValue("X 3", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue y3 = new NumberValue("Y 3", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue z3 = new NumberValue("Z 3", 1.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue x4 = new NumberValue("X 4", 1.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue y4 = new NumberValue("Y 4", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue z4 = new NumberValue("Z 4", 0.0F, -100.0F, 100.0F, NumberValue.Type.FLOAT);
    NumberValue angle1 = new NumberValue("Angle 1", 45.0F, -100.0, 360.0F, NumberValue.Type.FLOAT);
    NumberValue angle2 = new NumberValue("Angle 2", -20.0F, -100.0, 360.0F, NumberValue.Type.FLOAT);
    NumberValue angle3 = new NumberValue("Angle 3", -20.0F, -100.0, 360.0F, NumberValue.Type.FLOAT);
    NumberValue angle4 = new NumberValue("Angle 4", -80.0F, -100.0, 360.0F, NumberValue.Type.FLOAT);
    NumberValue scale = new NumberValue("Scale", 0.4F, 0.1F, 100.0F, NumberValue.Type.FLOAT);

    public void onUpdate(EventUpdate event) {
        push.setHidden(!mode.getValue().equals("Push"));

        // custom
        restore.setHidden(!mode.getValue().equals("Custom"));
        x1.setHidden(!mode.getValue().equals("Custom"));
        y1.setHidden(!mode.getValue().equals("Custom"));
        z1.setHidden(!mode.getValue().equals("Custom"));
        x2.setHidden(!mode.getValue().equals("Custom"));
        y2.setHidden(!mode.getValue().equals("Custom"));
        z2.setHidden(!mode.getValue().equals("Custom"));
        x3.setHidden(!mode.getValue().equals("Custom"));
        y3.setHidden(!mode.getValue().equals("Custom"));
        z3.setHidden(!mode.getValue().equals("Custom"));
        angle1.setHidden(!mode.getValue().equals("Custom"));
        angle2.setHidden(!mode.getValue().equals("Custom"));
        angle3.setHidden(!mode.getValue().equals("Custom") && !spin.getValue());
        angle4.setHidden(!mode.getValue().equals("Custom"));
        x4.setHidden(!mode.getValue().equals("Custom"));
        y4.setHidden(!mode.getValue().equals("Custom"));
        z4.setHidden(!mode.getValue().equals("Custom"));
        spin.setHidden(!mode.getValue().equals("Custom"));
        scale.setHidden(!mode.getValue().equals("Custom"));
        spinAngle.setHidden(!mode.getValue().equals("Custom") && !spin.getValue());
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {

            if(restore.getValue()) {
                x1.setValue(0.0F);
                y1.setValue(1.0F);
                z1.setValue(0.0F);
                x2.setValue(0.0F);
                y2.setValue(1.0F);
                z2.setValue(0.0F);
                x3.setValue(0.0F);
                y3.setValue(0.0F);
                z3.setValue(1.0F);
                x4.setValue(1.0F);
                y4.setValue(0.0F);
                z4.setValue(0.0F);
                angle1.setValue(45.0F);
                angle2.setValue(-20.0F);
                angle3.setValue(-20.0F);
                angle4.setValue(-80.0F);
                scale.setValue(0.4F);
                spin.setValue(false);
                spinAngle.setValue(360.0F);
                restore.setValue(false);
            }
        }
    }
}
