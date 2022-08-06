package slice.module.modules.misc;

import slice.event.data.EventInfo;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;
import slice.setting.settings.NumberValue;

@ModuleInfo(name = "Spammer", description = "Spams a chat message", category = Category.MISC)
public class ChatSpammer extends Module {

    NumberValue delay = new NumberValue("Delay", 1500L, 250L, 5000L, NumberValue.Type.LONG);

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if (timer.hasReached(delay.getValue().longValue())) {
            mc.thePlayer.sendChatMessage("Slice Client | gg/7T7yU2AWQK | " +  (int) Math.floor(Math.random() * 10000));
            timer.reset();
        }
    }

}
