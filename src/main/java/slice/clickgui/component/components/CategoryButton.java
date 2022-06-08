package slice.clickgui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.module.ModuleButton;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.module.data.Category;

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

    private int scroll;

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

    public void updateButtons() {
        int yAdd = 45;
        int index = 0;
        for(ModuleButton button : buttons) {
            TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

            button.setX(Slice.INSTANCE.getClickGui().getX() + 8);
            button.setY(Slice.INSTANCE.getClickGui().getY() + yAdd);

            button.setWidth((Slice.INSTANCE.getClickGui().getWidth())-10);

            button.setHeight((int) (font.getHeight(button.getName()) + 5));

            yAdd += /*font.getHeight(button.getName())*/ buttons.get(index).getHeight() + 20;
            index++;
        }
    }

    public void onClickGuiDrag(int mouseX, int mouseY) {
        updateButtons();
    }

    /**
     * Add all buttons to this category
     * */
    public void addButtons() {
        int yAdd = 45;
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
        for(Module module : Slice.INSTANCE.getModuleManager().getModules(parent)) {
            buttons.add(new ModuleButton(module, Slice.INSTANCE.getClickGui().getX() + 8, Slice.INSTANCE.getClickGui().getY() + yAdd, (Slice.INSTANCE.getClickGui().getWidth())-10, (int) (font.getHeight(module.getName()) + 5)));
            yAdd += font.getHeight(module.getName()) + 20;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            Slice.INSTANCE.getClickGui().setCategory(parent);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        }

        if(mouseButton == 1) {
            updateButtons();
        }

        if(Slice.INSTANCE.getClickGui().getCategory().equals(parent)) {
            buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }
}
