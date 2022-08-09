package slice.clickgui.pane;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.clickgui.module.ModuleButton;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.module.data.Category;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * it's like a dropdown menu for the ClickGui
 *
 * @author Nick
 * */
@Getter @Setter
public class DropdownPane {

    /** data */
    private Category category;
    private int x, y, width, height;

    /** Buttons */
    private List<ModuleButton> moduleButtons = new ArrayList<>();

    /** dragging */
    private int dragX, dragY;
    private boolean open, dragging;

    private int openHeight;

    private Module openModule;

    public DropdownPane(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public void drawPane(int mouseX, int mouseY) {
        moduleButtons = new ArrayList<>();

        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Black", 25);

        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        width = (int) (font.getWidth(category.getName()) + 20);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();

        int yAdd = 20;
        for(Module module : Slice.INSTANCE.getModuleManager().getModules(category)) {
            TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
            moduleButtons.add(new ModuleButton(module, x + 11, y + yAdd, (int) (font2.getWidth(module.getName()) + 20), (int)font2.getHeight(module.getName())));
            yAdd += font2.getHeight(module.getName())+5;
            openHeight += yAdd;
        }


        if(open) {
            int add = 10;
            RenderUtil.drawRoundedRect((x-add) + 20, y, x + (width+add), y + yAdd, 15, new Color(1, 0, 0, 155).getRGB());
        }

        GlStateManager.color(255, 255, 255, 155);
        RenderUtil.drawRoundedRect(x, y, x + (width+20), y + 20, 10, new Color(255,171,171).getRGB());

        GlStateManager.popMatrix();
        Gui.drawCenteredString(font, category.getName(), x+((float)width/2)+9, y + ((height/2f)+1), -1);

        if(open) moduleButtons.forEach(moduleButton -> moduleButton.drawButton(mouseX, mouseY));

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        openHeight = 0;
    }

    @SuppressWarnings("all")
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (dragging) {
            dragging = false;
        }
        if(open) moduleButtons.forEach(moduleButton -> moduleButton.mouseReleased(mouseX, mouseY, mouseButton));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(open) moduleButtons.forEach(moduleButton -> moduleButton.mouseClicked(mouseX, mouseY, mouseButton));

        if ((mouseX >= x && mouseX <= x + (width+20) && mouseY >= y && mouseY <= y + 20) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }

        /* opening the gui */
        if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20) {

            if(mouseButton == 1)
                open = !open;

        }
    }

}
