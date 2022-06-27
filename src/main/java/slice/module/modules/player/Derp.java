package slice.module.modules.player;

import java.util.concurrent.ThreadLocalRandom;
import slice.event.Event;
import slice.event.events.EventUpdate;
import slice.module.Module;
import slice.module.data.Category;
import slice.module.data.ModuleInfo;

@ModuleInfo(name = "Derp", description = "Makes you spin around", category = Category.PLAYER)
public class Derp extends Module {

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            int randomYaw = ThreadLocalRandom.current().nextInt(-180, 180 + 1);
            int randomPitch = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
            e.setYaw(randomYaw);
            e.setPitch(randomPitch);
        }
    }
}
