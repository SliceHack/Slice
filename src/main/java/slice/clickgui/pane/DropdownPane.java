package slice.clickgui.pane;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.module.Module;
import slice.module.data.Category;
import slice.util.RenderUtil;

import java.awt.*;

@Getter @Setter
public class DropdownPane {

    /** data */
    private Category category;
    private int x, y, width, height;

    /** dragging */
    private int dragX, dragY;
    private boolean open, dragging;

    public DropdownPane(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public void drawPane(int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Black", 25);
        TTFFontRenderer font2 = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin", 20);

        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        width = (int) (font.getWidth(category.getName()) + 20);

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();

        if(open) {
            int add = 10;
            RenderUtil.drawRoundedRect((x-add) + 20, y, x + (width+add), y + 100, 15, new Color(1, 0, 0, 155).getRGB());
        }
        GlStateManager.color(255, 255, 255, 155);
        RenderUtil.drawRoundedRect(x, y, x + (width+20), y + 20, 10, new Color(255, 165, 255).getRGB());
        GlStateManager.popMatrix();
        font.drawCenteredString(category.getName(), x+((float)width/2)+9, y + (height/2f), -1);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
    }

    @SuppressWarnings("all")
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (dragging) {
            dragging = false;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
