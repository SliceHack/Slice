package slice.notification;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.events.EventUpdate;
import slice.util.LoggerUtil;
import slice.util.Timer;

@Getter @Setter
public class Notification {

    private String title, message;
    private double seconds;

    private Timer timer = new Timer();
    private boolean ran;

    public Notification(Type type, String message, double seconds) {
        this.title = type.getName();
        this.message = message;
        this.seconds = seconds;
        Slice.INSTANCE.getEventManager().register(this);
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(timer.hasTimeReached(1000)) {
            timer.reset();
        }
    }
}
