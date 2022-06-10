package slice.module.modules.misc;

import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Spammer", description = "Spams a chat message", category = Category.MISC)
public class ChatSpammer extends Module {

    NumberValue delay = new NumberValue("Delay", 1500L, 250L, 5000L, NumberValue.Type.LONG);

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            if (timer.hasReached(delay.getValue().longValue())) {
                mc.thePlayer.sendChatMessage("Slice Client | gg/ | " +  (int) Math.floor(Math.random() * 10000));
                timer.reset();
            }
        }
    }
}
