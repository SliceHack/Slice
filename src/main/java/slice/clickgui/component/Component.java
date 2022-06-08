package slice.clickgui.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Component {

    private String name;
    private int x, y, width, height;

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {}
    public void mouseClicked(int mouseX, int mouseY) {}

}
