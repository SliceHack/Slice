package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import slice.event.Event;

import java.util.List;

@Getter
@AllArgsConstructor
public class EventActionPerformed extends Event {
    private GuiButton button;
    private GuiScreen screen;
}
