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
    private List<EventSender> registeredSenders = new ArrayList<>();

    /**
     * Register an object to the event manager.
     *
     * @param object The object to register.
     * */
    public void register(Object object) {
        if(isRegistered(object))
            return;

        registeredObjects.add(object);
    }

    /**
     * unregister an object from the event manager.
     *
     * @param object The object to unregister.
     * */
    public void unregister(Object object) {
        if(!isRegistered(object))
            return;

        registeredObjects.remove(object);
    }

    /**
     * Checks if an object is registered.
     *
     * @param object The object to check.
     * */
    public boolean isRegistered(Object object) {
        return registeredObjects.contains(object);
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

                    List<EventSender> registeredSenders = new ArrayList<>(this.registeredSenders);
                    if(getEventSender(event, method, object) != null) getEventSender(event, method, object).runEvent();
                    else registeredSenders.add(new EventSender(event, method, object));
                    this.registeredSenders = registeredSenders;
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
    public EventSender getEventSender(Event event, Method method, Object object) {
        List<EventSender> registeredSenders = new ArrayList<>(this.registeredSenders);
        for(EventSender sender : registeredSenders) {
            if(sender.getEvent().equals(event) && sender.getMethod().equals(method) && sender.getObject().equals(object))
                return sender;
        }
        return null;
    }
}
