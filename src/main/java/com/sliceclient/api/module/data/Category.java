package com.sliceclient.api.module.data;

import lombok.Getter;

/**
 * The category of the module.
 *
 * @author Nick
 * @since 1/2/2023
 * */
@Getter
public enum Category {
    COMBAT, MOVEMENT,
    PLAYER, WORLD,
    RENDER, MISC;
}
