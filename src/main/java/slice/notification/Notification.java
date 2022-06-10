package slice.notification;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Notification {

    private String title, message;

    public Notification(Type type, String message, int seconds) {
        this.title = type.getName();
        this.message = message;
    }

    public void draw() {

    }
}
