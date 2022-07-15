package slice.event;

import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class EventManager {

    /* ArrayList */
    private final HashMap<Object, Method> methods = new HashMap<>();

    /**
     * Where every event is registered.
     * @param event the event to run
     */
    public void onEvent(Event event) {
        try {
            methods.forEach((key, value) -> {
                if (value.getParameterTypes()[0].equals(event.getClass())) {
                    call(key, value, event);
                }
            });
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    /**
     * Registers an object
     * @parma object The object to register
     * */
    public void register(Object object) {
        for(Method method : object.getClass().getMethods()) {
            if(method.isAnnotationPresent(EventInfo.class) && !methods.containsKey(method)) {
                Class<?>[] args = method.getParameterTypes();

                if(args.length >= 1) {
                    Class<?> event = args[0];
                    if(event.getSuperclass().equals(Event.class)) {
                        methods.put(object, method);
                    }
                }
            }
        }
    }

    /**
     * Unregisters a method
     * */
    public void unRegister(Object object) {
        methods.remove(object);
    }

    /**
     * calls a method
     */
    public void call(Object obj, Method m, Object... args) {
        try {
            m.setAccessible(true);
            m.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
