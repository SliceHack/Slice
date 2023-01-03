package com.sliceclient.api.exceptions;

import com.sliceclient.api.command.data.CommandInfo;

/**
 * Thrown when a module is missing the {@link CommandInfo} annotation.
 *
 * @author Nick
 * @since 1/3/23
 */
public class CommandInfoNotFoundException extends Exception {

    /**
     * Constructs a new CommandInfoNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandInfoNotFoundException(String message) {
        super(message);
    }

}
