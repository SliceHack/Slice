package slice.clickgui.component.components;

import lombok.Getter;
import lombok.Setter;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.font.TTFFontRenderer;
import slice.module.data.Category;

import java.awt.*;

/**
 * Client CategoryButton
 *
 * @author Nick
 * */
@Getter @Setter
public class CategoryButton extends Component {

    private Category parent;

    public CategoryButton(Category category, int x, int y, int width, int height) {
        super(category.getName(), x, y, width, height);
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

        font.drawString(parent.getName(), getX(), getY(), isHovered(mouseX, mouseY) ? Color.ORANGE.darker().getRGB() : -1);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if(isHovered(mouseX, mouseY)) Slice.INSTANCE.getClickGui().setCategory(parent);
    }
}
