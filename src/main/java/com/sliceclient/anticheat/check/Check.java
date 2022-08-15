package com.sliceclient.anticheat.check;

import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.user.User;
import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.util.LoggerUtil;

/**
 * The Check class is the base class for all checks.
 *
 * @author Nick
 */
@Getter @Setter
public class Check {

    /** CheckInfo */
    private final CheckInfo info = getClass().getAnnotation(CheckInfo.class);

    /** Info */
    private final String name, type, description;

    /** User */
    protected User user;

    /**
     * Constructs a new Check.
     */
    public Check() {
        if(info == null) throw new IllegalArgumentException("CheckInfo is null.");

        name = info.name();
        type = info.type();
        description = info.description();
    }

    /**
     * Flags the check as failed.
     * */
    public void flag() {
        if (!Slice.INSTANCE.getModuleManager().getModule("AntiCheat").isEnabled()) return;
        LoggerUtil.addMessage("&c" + user.getPlayer().getName() + " &7has failed &c" + name + " &7(&cType " + type + "&7)");
    }

    /**
     * Prints a debug message to the chat.
     * @param message The message to print.
     * */
    public void debug(String message) {
        LoggerUtil.addMessage("&7[&cDEBUG&7] &c" + user.getPlayer().getName() + " &7" + message);
    }
}
