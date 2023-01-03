package com.sliceclient.api.command.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a command.
 *
 * @author Nick
 * @since 1/3/23
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();
    String description() default "No description provided.";
    String[] aliases() default {};
}
