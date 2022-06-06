package slice.event;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;

@Getter @Setter
public class Event {
    public boolean cancelled;

    public void call() {
        Slice.INSTANCE.onEvent(this);
    }
}
