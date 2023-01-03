package com.sliceclient.api;

import com.sliceclient.api.command.Command;
import com.sliceclient.api.module.Module;
import slice.Slice;

/**
 * You use this to use the Slice API.
 *
 * @author Nick
 * @since 1/2/2023
 * */
public class SlicePlugin {

    /**
     * Override this method
     * */
    public void initialize() {}
    public void shutdown() {}

    /**
     * This is used to register a module.
     *
     * @param module the module to register
     */
    public void registerModule(Module module) {
        Slice.INSTANCE.getModuleManager().register(module);
    }


    /**
     * This is used to register a command.
     *
     * @param command the command to register
     */
    public void registerCommand(Command command) {
        Slice.INSTANCE.getCommandManager().register(command);
    }
}
