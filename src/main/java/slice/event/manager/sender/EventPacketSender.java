package slice.event.manager.sender;

import lombok.Getter;
import net.minecraft.network.Packet;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventPacket;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The class used to send all events.
 *
 * @author Nick
 * */
@Getter
public class EventPacketSender {

    /** The event to send */
    private final Packet<?> event;
    private final EventPacket eventPacket;

    /** The method to send */
    private final Method method;

    /** The object to send */
    private final Object object;

    /**
     * To Avoid one class running to many events at the same time.
     *
     * @param event The event to run.
     * @param method The method to run.
     * @param parent The object to run the method on.
     * */
    public EventPacketSender(Packet<?> event, Method method, Object parent, EventPacket eventPacket) {
        this.event = event;
        this.method = method;
        this.object = parent;
        this.eventPacket = eventPacket;

        runEvent();
    }

    /**
     * To run the event.
     * */
    public synchronized void runEvent() {
        try {
            if(!method.isAccessible()) method.setAccessible(true);
            if(!hasAnnotation(method, PacketEvent.class)) return;
            if(!getMethodParameterType(method, 0).equals(event.getClass())) return;

            switch (getMethod().getParameterTypes().length) {
                case 1:
                    method.invoke(object, event);
                    break;
                case 2:
                    if(!getMethodParameterType(method, 1).equals(eventPacket.getClass())) return;

                    method.invoke(object, event, eventPacket);
                    break;
            }
        } catch (Exception ignored) {}
    }

    /**
     * Checks if a method has an annotation.
     *
     * @param method The method to check.
     * @param annotation The annotation to check for.
     * @return If the method has the annotation.
     * */
    public boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
        return method.isAnnotationPresent(annotation);
    }


    /**
     * Gets parameters from a method.
     *
     * @param method The method to get the parameters from.
     * @parma index The index of the parameter.
     * @return The parameter.
     * */
    public Class<?> getMethodParameterType(Method method, int index) {
        return method.getParameterTypes()[index];
    }
}
