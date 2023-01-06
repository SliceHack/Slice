package com.sliceclient.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Location {
    private double x, y, z;
    private float yaw, pitch;

    public Location() {
        this(0, 0, 0, 0, 0);
    }

    public Location setX(final double x) {
        this.x = x;
        return this;
    }

    public Location setY(final double y) {
        this.y = y;
        return this;
    }

    public Location setZ(final double z) {
        this.z = z;
        return this;
    }

    public Location setYaw(final float yaw) {
        this.yaw = yaw;
        return this;
    }

    public Location setPitch(final float pitch) {
        this.pitch = pitch;
        return this;
    }
}
