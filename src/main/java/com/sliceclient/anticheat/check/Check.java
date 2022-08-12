package com.sliceclient.anticheat.check;

import com.sliceclient.anticheat.check.data.CheckInfo;
import com.sliceclient.anticheat.user.User;
import lombok.Getter;
import lombok.Setter;

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
}
