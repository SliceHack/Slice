package slice.clickgui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
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

        font.drawString(getName(), getX(), getY(), (isHovered(mouseX, mouseY) || Slice.INSTANCE.getClickGui().getCategoryButton(parent).equals(this)) ? Color.ORANGE.getRGB() : -1);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if(isHovered(mouseX, mouseY)) {
            Slice.INSTANCE.getClickGui().setCategory(parent);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }
}
