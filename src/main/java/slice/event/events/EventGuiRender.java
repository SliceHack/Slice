package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import slice.event.Event;

@Getter @Setter
@AllArgsConstructor
public class EventGuiRender extends Event {
    private ScaledResolution sr;
    private int width, height;
    private float partialTicks;
}
