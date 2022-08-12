package com.sliceclient.anticheat.check.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The CheckInfo class for the information of a check.
 *
 * @author Nick
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInfo {
    String name();
    String type() default "A";
    String description() default "No description available.";
}