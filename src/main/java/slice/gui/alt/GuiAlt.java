package slice.gui.alt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiAlt extends GuiScreen {

    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 50, 200, 20, "Mojang"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2, 200, 20, "TheAltening"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 50, 200, 20, "Microsoft"));
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
        }
    }
}
