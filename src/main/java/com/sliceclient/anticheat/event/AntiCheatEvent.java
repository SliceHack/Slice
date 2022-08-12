package com.sliceclient.anticheat.event;

import com.sliceclient.anticheat.SliceAC;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The Anti-Cheat event class.
 *
 * @author Nick
 * */
@Getter
public class AntiCheatEvent {
    protected EntityPlayer player;

    /**
     * Calls the event.
     * */
    public void call() {
        SliceAC.INSTANCE.getEventManager().runEvent(this);
    }
}
