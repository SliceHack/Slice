package slice.module.modules.combat;

import org.lwjgl.input.Mouse;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.ModeValue;
import slice.setting.settings.NumberValue;

import static org.apache.commons.lang3.ArrayUtils.add;

@ModuleInfo(name = "AutoClicker", description = "Clicks for you when you hold down left click", category = Category.COMBAT)
public class AutoClicker extends Module {

    ModeValue mode = new ModeValue("Mode", "CPS", "CPS", "Legit");
    NumberValue cps = new NumberValue("CPS", 15, 2, 20, NumberValue.Type.INTEGER);

    NumberValue maxCps = new NumberValue("Maximum CPS", 20, 2, 20, NumberValue.Type.INTEGER);
    NumberValue minCps = new NumberValue("Minimum CPS", 20, 2, 20, NumberValue.Type.INTEGER);

    private int[] clickPattern = {};
    private int lastMax, lastMin;
    private int index;

    public void onUpdate(EventUpdate event) {
        cps.setHidden(!mode.getValue().equals("CPS"));
    }

    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {

            mc.gameSettings.keyBindAttack.pressed = false;

            if((maxCps.getValue().intValue() != lastMax) || (minCps.getValue().intValue() != lastMin)) {
                for(int i = minCps.getValue().intValue(); i <= maxCps.getValue().intValue(); i++) {
                    clickPattern = add(clickPattern, i);
                }
            }

            lastMax = maxCps.getValue().intValue();
            lastMin = minCps.getValue().intValue();

            if(Mouse.isButtonDown(0)) {
                switch (mode.getValue()) {
                    case "CPS":
                        if(timer.hasReached(1000 / cps.getValue().intValue())) {
                            mc.clickMouse();
                            timer.reset();
                        }
                        break;
                    case "Legit":
                        if(timer.hasReached(1000 / clickPattern[index])) {
                            mc.clickMouse();
                            index++;
                            if(index >= clickPattern.length) index = clickPattern.length;
                            timer.reset();
                        }
                        break;
                }
                return;
            }

            if(timer.hasReached(1000)) {
                index = 0;
                timer.reset();
            }

        }
    }

}
