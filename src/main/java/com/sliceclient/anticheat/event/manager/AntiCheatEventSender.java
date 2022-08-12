package com.sliceclient.anticheat.event.manager;

import com.sliceclient.anticheat.event.AntiCheatEvent;
import lombok.Getter;
import slice.util.LoggerUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The AntiCheat event sender for the manager.
 *
 * @author Nick
 */
@Getter
public class AntiCheatEventSender {

    private final AntiCheatEvent event;
    private final Method method;
    private final Object target;

    public AntiCheatEventSender(AntiCheatEvent event, Method method, Object target) {
        this.event = event;
        this.method = method;
        this.target = target;
        this.execute();
    }

    public void execute() {
        try {
            if(!hasAnnotation(method, AntiCheatEventInfo.class)) return;
            if(method.getParameterTypes().length != 1) return;
            if(!getMethodParameterType(method, 0).equals(event.getClass())) return;

            method.invoke(target, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
