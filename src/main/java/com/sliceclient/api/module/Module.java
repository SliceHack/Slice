package com.sliceclient.api.module;

import com.sliceclient.api.exceptions.ModuleInfoNotFoundException;
import com.sliceclient.api.module.data.ModuleInfo;
import com.sliceclient.api.player.Player;
import lombok.Getter;
import slice.module.data.Category;

/**
 * This is the base for scripted Modules.
 *
 * @author Nick
 * @since 1/2/2023
 */
@SuppressWarnings("unused")
public class Module extends slice.module.Module {

    private final Player player = new Player();

    public Module() {
        try {
            if (getClass().getAnnotation(ModuleInfo.class) == null) {
                throw new ModuleInfoNotFoundException("Module " + getClass().getSimpleName() + " is missing the ModuleInfo annotation.");
            }

            name = getClass().getAnnotation(ModuleInfo.class).name();
            description = getClass().getAnnotation(ModuleInfo.class).description();
            category = Category.translate(getClass().getAnnotation(ModuleInfo.class).category());
        } catch (ModuleInfoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}