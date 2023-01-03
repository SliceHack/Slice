package com.sliceclient.api.event;

import lombok.Getter;
import slice.Slice;

@Getter
public class Event extends slice.event.Event {
    public boolean cancelled;

    public void call() {
        Slice.INSTANCE.getEventManager().register(this);
    }
}