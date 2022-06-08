package slice.clickgui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.module.ModuleButton;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.module.data.Category;
import slice.util.LoggerUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Client CategoryButton
 *
 * @author Nick
 * */
@Getter @Setter
public class CategoryButton extends Component {

    private Category parent;
    private int scrollHeight;

    private List<ModuleButton> buttons = new ArrayList<>();

    public CategoryButton(Category category, int x, int y, int width, int height) {
        super(category.getName(), x, y, width, height);
        this.parent = category;
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        font.drawString(getName(), getX(), getY(), Slice.INSTANCE.getClickGui().getCategory().equals(parent) ? Color.ORANGE.getRGB() : -1);
        GlStateManager.popMatrix();


        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int yAdd = 0;
        buttons.forEach(button -> button.drawComponent(mouseX, mouseY, partialTicks));
        GL11.glPopMatrix();
    }

    public void addButtons() {
        for(Module module : )
    }

    public void onScroll(int scrollDelta) {
        scrollHeight += scrollDelta * 5f;
        if (scrollHeight < 0) scrollHeight = 0;
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if(isHovered(mouseX, mouseY)) {
            Slice.INSTANCE.getClickGui().setCategory(parent);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        }
        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY));
    }
}
