package slice.event.manager;

import lombok.Getter;
import net.minecraft.network.Packet;
import slice.event.Event;
import slice.event.data.EventInfo;
import slice.event.data.PacketEvent;
import slice.event.events.EventPacket;
import slice.event.manager.sender.EventPacketSender;
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

        if(event instanceof EventPacket) {
            EventPacket eventPacket = (EventPacket) event;
            Packet<?> p = eventPacket.getPacket();
            onPacket(p, eventPacket);
        }

        for (Object object : registeredObjects) {
            for (Method method : getMethods(object.getClass())) {

                if(method.getParameterTypes().length == 1
                        && method.isAnnotationPresent(EventInfo.class)
                        && (method.getParameterTypes()[0].equals(event.getClass()) || method.getParameterTypes()[0].equals(Event.class))) {
                    new EventSender(event, method, object);
                }
            }
        }
    }

    /**
     * Runs all the packet events.
     *
     * @param packet The packet event to run.
     * */
    public void onPacket(Packet<?> packet, EventPacket e) {
        List<Object> registeredObjects = new ArrayList<>(this.registeredObjects);

        for (Object object : registeredObjects) {
            for (Method method : getMethods(object.getClass())) {

                if(method.getParameterTypes().length >= 1 && method.getParameterTypes().length <= 2
                        && method.isAnnotationPresent(PacketEvent.class)
                        && (method.getParameterTypes()[0].equals(packet.getClass()) || method.getParameterTypes()[0].equals(Packet.class))) {
                    new EventPacketSender(packet, method, object, e);
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
        return this.registeredSenders.stream().filter(eventSender -> eventSender.getMethod().equals(method) && eventSender.getObject().equals(object)).findFirst().orElse(null);
    }
}
