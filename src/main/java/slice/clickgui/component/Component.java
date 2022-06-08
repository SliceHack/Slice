package slice.clickgui.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Base of every component on the ClickGui
 *
 * @author Nick
 * */
@Getter @Setter
@AllArgsConstructor
public class Component {

    private String name;
    private int x, y, width, height;

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {}
    public void mouseClicked(int mouseX, int mouseY) {}

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

}
