package com.sliceclient.api.module;

import com.sliceclient.api.exceptions.ModuleInfoNotFoundException;
import com.sliceclient.api.module.data.ModuleInfo;
import lombok.Getter;
import slice.module.data.Category;

/**
 * This is the base for scripted Modules.
 *
 * @author Nick
 * @since 1/2/2023
 */
@Getter
@SuppressWarnings("unused")
public class Module extends slice.module.Module {

    /** the info of the module */
    private final ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

    public Module() {
        try {
            if (info == null) {
                throw new ModuleInfoNotFoundException("Module " + getClass().getSimpleName() + " is missing the ModuleInfo annotation.");
            }

            name = info.name();
            description = info.description();
            category = Category.translate(info.category());
        } catch (ModuleInfoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}