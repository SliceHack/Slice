package com.sliceclient.api.module.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The info of the module.
 *
 * @author Nick
 * @since 1/2/2023
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();
    String description();
    Category category();
    int key() default 0;
}
