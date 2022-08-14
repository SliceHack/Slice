package com.sliceclient.anticheat.manager;

import com.sliceclient.anticheat.event.AntiCheatEvent;
import com.sliceclient.anticheat.event.manager.AntiCheatEventSender;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import slice.Slice;
import slice.module.modules.misc.AntiCheat;
import slice.util.LoggerUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The AntiCheat event manager.
 *
 * @author Nick
 */
@Getter
public class AntiCheatEventManager {

    /** The list of events to send. */
    private final HashMap<Object, EntityPlayer> registeredObjects = new HashMap<>();

    /**
     * runs all the events.
     *
     * @param event the event to run.
     */
    public void runEvent(AntiCheatEvent event) {
        registeredObjects.forEach((object, player) -> {
            if(event.getPlayer() == player) {
                try {
                    for(Method method : object.getClass().getMethods()) {
                        if(Slice.INSTANCE.getModuleManager().getModule(AntiCheat.class).isEnabled()) {
                            new AntiCheatEventSender(event, method, object);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * registers an object to the event manager.
     *
     * @param object the object to register.
     * @param player the player to register.
     */
    public void register(Object object, EntityPlayer player) {
        registeredObjects.put(object, player);
    }

    /**
     * unregisters an object from the event manager.
     *
     * @param object the object to unregister.
     */
    public void unregister(Object object) {
        registeredObjects.remove(object);
    }
}
