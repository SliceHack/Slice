package slice.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import slice.event.Event;

import java.util.List;

@AllArgsConstructor
@Getter
public class EventGUIInit extends Event {
    private GuiScreen screen;
    private List<GuiButton> buttonList;
}
