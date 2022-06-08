package slice.clickgui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import slice.Slice;
import slice.clickgui.component.Component;
import slice.clickgui.component.components.CategoryButton;
import slice.font.TTFFontRenderer;
import slice.module.data.Category;
import slice.util.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Client ClickGui
 *
 * @author Nick & Dylan
 * */
@Getter @Setter
public class ClickGui extends GuiScreen {

    /** the components of the ClickGui*/
    private List<Component> components = new ArrayList<>();

    /** For dragging the ClickGui */
    private int dragX, dragY;
    private boolean dragging;

    /** the positions of the ClickGui */
    private int x, y, width = 350, height = 350;

    /** data */
    private Category category;

    /**
     * Ran when the client is launched
     */
    public ClickGui() {
        this.category = Category.values()[0];
    }

    public void onGuiClosed() {
        components.clear();
    }

    public void initGui() {
        dragging = false;
        addComponents();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
            components.forEach(component -> component.onClickGuiDrag(mouseX, mouseY));
            updateComponents();
        }

        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, new Color(105, 101, 101).darker().getRGB());
        RenderUtil.drawRoundedRect(x - 5, y, x + (width+2), y + 30, 15, new Color(105, 101, 101).darker().darker().getRGB());
        RenderUtil.drawRoundedRect(x - 65, y, (x - 65) + 70, y + height, 15, new Color(105, 101, 101).darker().darker().getRGB());

        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
    }

    /**
     * Handles scrolling
     * */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if(i != 0) components.forEach(component -> component.onScroll(Integer.compare(i, 0)));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));

        if(isInsideOfDrag(mouseX, mouseY)) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    /**
     * Adds the componeents to the ClickGui
     * */
    public void addComponents() {
        if(components.size() > 0)
            return;

        int yAdd = 45;
        for(Category category : Category.values()) {
            TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

            components.add(new CategoryButton(category, x - (65), y + yAdd, (int) (font.getWidth(category.getName()) + 5), (int) (font.getHeight(category.getName()) + 2)));
            yAdd += 35;
        }
    }

    /** Update components */
    public void updateComponents() {
        int yAdd = 45;
        for(Component component : components) {
            if(component instanceof CategoryButton) {
                TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);

                component.setX(x - (65));
                component.setY(y + yAdd);
                component.setWidth((int) (font.getWidth(component.getName()) + 5));
                component.setHeight((int) (font.getHeight(component.getName()) + 2));
                yAdd += 35;
            }
        }
    }

    /**
     * Gets Category Button from the components
     */
    public CategoryButton getCategoryButton(Category category) {
        return (CategoryButton) components
                .stream()
                .filter(component -> component instanceof CategoryButton)
                .filter(component -> ((CategoryButton) component).getParent() == category)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if you are inside the drag component
     */
    public boolean isInsideOfDrag(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + (width+2) && mouseY >= y && mouseY <= y + 30;
    }

    /**
     * So the gui does not pause the game when it is open
     */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
