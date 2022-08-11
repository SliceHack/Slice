package slice.notification;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.cef.RequestHandler;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.events.EventUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Getter @Setter
public class NotificationManager {

    private Notification currentNotification;
    private List<Notification> notifications = new ArrayList<>();

    public NotificationManager() {
        Slice.INSTANCE.getEventManager().register(this);
    }

    @EventInfo
    public void onUpdate(EventUpdate e) {
        if(notifications.isEmpty()) return;

        currentNotification = notifications.get(0);

        if(!currentNotification.isRan()) {
            RequestHandler.addNotification(currentNotification);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notifications.remove(currentNotification);
                }
            }, 1000 * ((long)currentNotification.getSeconds()));
            currentNotification.setRan(true);
        }
    }

    public void queueNotification(Notification notification) {
        notifications.add(notification);
    }
}
