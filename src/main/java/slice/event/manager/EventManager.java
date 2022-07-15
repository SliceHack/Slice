package slice.event.manager;

import lombok.Getter;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.manager.sender.EventSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The manager for all events.
 *
 * @author Nick
 * */
@Getter
public class EventManager {

    /** The registered objects */
    private final List<Object> registeredObjects = new ArrayList<>();

    /** The registered methods */
    private final List<EventSender> registeredSenders = new ArrayList<>();

    /**
     * Register an object to the event manager.
     *
     * @param object The object to register.
     * */
    public void register(Object object) {
        registeredObjects.add(object);
    }

    /**
     * unregister an object from the event manager.
     *
     * @param object The object to unregister.
     * */
    public void unregister(Object object) {
        registeredObjects.remove(object);
    }

    /**
     * Runs all events.
     *
     * @param event The event to run.
     * */
    public void runEvent(Event event) {
        List<Object> registeredObjects = new ArrayList<>(this.registeredObjects);

        for (Object object : registeredObjects) {

            for (Method method : getMethods(object.getClass())) {

                if(method.getParameterTypes().length == 1
                        && method.isAnnotationPresent(EventInfo.class)
                        && method.getParameterTypes()[0].equals(event.getClass())) {

                    new EventSender(event, method, object);
                }
            }
        }
    }

    /**
     * Gets all methods from a class.
     *
     * @param clazz The class to get the methods from.
     * @return The methods.
     * */
    public Method[] getMethods(Class<?> clazz) {
        return clazz.getMethods();
    }

    /**
     * Gets the EventSender from a method.
     *
     * @param method The method to get the EventSender from.
     * @return The EventSender.
     * */
    public EventSender getEventSender(Method method) {
        for (EventSender sender : registeredSenders) {
            if (sender.getMethod().equals(method)) {
                return sender;
            }
        }
        return null;
    }
}
