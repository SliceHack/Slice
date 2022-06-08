package slice.clickgui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
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

    private List<ModuleButton> buttons = new ArrayList<>();

    public CategoryButton(Category category, int x, int y, int width, int height) {
        super(category.getName(), x, y, width, height);
        this.parent = category;
        addButtons();
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        font.drawString(getName(), getX(), getY(), Slice.INSTANCE.getClickGui().getCategory().equals(parent) ? Color.ORANGE.getRGB() : -1);
        GlStateManager.popMatrix();


        if(Slice.INSTANCE.getClickGui().getCategory().equals(parent)) {
            buttons.forEach(button -> button.drawComponent(mouseX, mouseY, partialTicks));
        }
    }

    public void addButtons() {
        int yAdd = 0;
        for(Module module : Slice.INSTANCE.getModuleManager().getModules(parent)) {
            buttons.add(new ModuleButton(module, getX(), getY() + yAdd, getWidth(), 25));
            yAdd += 25;
            LoggerUtil.addMessage("Added button from module: " + module.getName());
        }
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if(Slice.INSTANCE.getClickGui().getCategory().equals(parent)) {
            if (isHovered(mouseX, mouseY)) {
                Slice.INSTANCE.getClickGui().setCategory(parent);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            }
            buttons.forEach(button -> button.mouseClicked(mouseX, mouseY));
        }
    }
}
