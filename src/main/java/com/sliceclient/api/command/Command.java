package com.sliceclient.api.command;

import com.sliceclient.api.command.data.CommandInfo;
import com.sliceclient.api.exceptions.CommandInfoNotFoundException;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a command.
 *
 * @author Nick
 * @since 1/3/23
 */
@Getter @Setter
public abstract class Command extends slice.command.Command {

    /**
     * Constructs a new Command.
     */
    public Command() {
        try {
            CommandInfo info = getClass().getAnnotation(CommandInfo.class);
            if (info == null) {
                throw new CommandInfoNotFoundException("Command class must have @CommandInfo annotation");
            }

            setName(info.name());
            setDescription(info.description());
            setAliases(info.aliases());
        } catch (CommandInfoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
