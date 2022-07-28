package slice.notification;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import slice.util.LoggerUtil;
import slice.util.Timer;

@Getter @Setter
public class Notification {

    private String title, message;

    public Notification(Type type, String message, int seconds) {
        this.title = type.getName();
        this.message = message;
    }

    public void draw() {}

}
