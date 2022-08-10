package slice.gui.main;

import net.minecraft.client.Minecraft;
import org.cef.ccbluex.GuiView;
import org.cef.ccbluex.Page;
import slice.util.LoggerUtil;

import java.io.File;

public class NewMainMenu extends GuiView {

    public NewMainMenu() {
        super(new Page(Minecraft.getMinecraft().mcDataDir + File.separator +  "Slice" + File.separator +  "html" + File.separator +  "gui" + File.separator + "MainMenu" + File.separator + "index.html"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int key) {
        super.mouseClicked(mouseX, mouseY, key);
    }
}
