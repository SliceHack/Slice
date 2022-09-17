package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class Event2DIgnoreGUI extends Event {
    private float partialTicks;
    private int width, height;
    private ScaledResolution scaledResolution;
}
