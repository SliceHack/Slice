package slice.gui.alt;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.font.TTFFontRenderer;

import java.io.IOException;
import java.net.Proxy;

@Getter @Setter
@Deprecated
public class GuiOfflineLogin extends GuiScreen {

    private GuiTextField token;
    private GuiButton login;
    private String loginText, generateToken;

    private GuiScreen parent;

    public GuiOfflineLogin(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
        this.token = new GuiTextField(height / 4 + 24, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.login = new GuiButton(0, width / 2 - 100, height / 2 + 20, 200, 20, "Login");
        this.buttonList.add(this.login);
        loginText = null;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "Offline Login", width / 2, 20, 16777215);
        if(loginText != null) {
            TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
            drawCenteredString(font, loginText, (float)width / 2, (float)height / 2, -1);
        }
        token.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(mouseX >= this.login.xPosition && mouseX <= this.login.xPosition + this.login.getButtonWidth() && mouseY >= this.login.yPosition && mouseY <= this.login.yPosition + this.login.getButtonWidth()) {
            if(this.token.getText().isEmpty())
                return;

            login(this.token.getText());
        }
    }

    public void login(String t) {
        Minecraft.getMinecraft().session = new Session(t, "0", "0", "legacy");
        loginText = "Logged in as " + Minecraft.getMinecraft().session.getUsername();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.parent);
        }
        token.textboxKeyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        token.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
        token.updateCursorCounter();
    }
}
