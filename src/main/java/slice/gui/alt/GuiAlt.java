package slice.gui.alt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@Deprecated
public class GuiAlt extends GuiScreen {

    private final GuiScreen parent;

    public GuiAlt(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 50, 200, 20, "Mojang"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2, 200, 20, "TheAltening"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 50, 200, 20, "Microsoft"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + 100, 200, 20, "Offline"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(new MojangAltLogin(this));
                break;
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(new TheAlteningAPILogin(this));
                break;
            case 2:
                Minecraft.getMinecraft().displayGuiScreen(new MicrosoftAltLogin(this));
                break;
            case 3:
                Minecraft.getMinecraft().displayGuiScreen(new GuiOfflineLogin(this));
                break;
        }
    }
}
