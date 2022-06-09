package slice.gui.alt;

import com.thealtening.AlteningServiceType;
import com.thealtening.SSLController;
import com.thealtening.TheAlteningAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.LoggerUtil;
import slice.util.account.LoginUtil;
import slice.util.account.microsoft.MicrosoftAccount;
import the_fireplace.ias.gui.GuiPasswordField;

import java.io.IOException;

public class MicrosoftAltLogin extends GuiScreen {

    GuiScreen parent;

    GuiTextField username;
    GuiPasswordField password;
    String loginText;
    private SSLController ssl = new SSLController();
    private TheAlteningAuthentication serviceSwitch = TheAlteningAuthentication.mojang();

    public MicrosoftAltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        // add username field
        int x = (width / 2) - 100;
        int y = (height / 2) - 50;
        username = new GuiTextField(0, fontRendererObj, x, y, 200, 20);

        // add password field
        x = (width / 2) - 100;
        y = (height / 2) - 20;
        password = new GuiPasswordField(0, fontRendererObj, x, y, 200, 20);

        this.buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 25, 200, 20, "Login"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Thin",  35);

        font.drawCenteredString("Microsoft Alt Login", (sr.getScaledWidth() / 2f)+3, 25, -1);

        username.drawTextBox();
        password.drawTextBox();

        if(loginText != null) {
            font.drawCenteredString(loginText, (sr.getScaledWidth() / 2f)+3, height / 2f + 25, -1);
        }

        font.drawString("Username:", ((sr.getScaledWidth() / 2f)-100)-font.getWidth("Username: "), height / 2f - 50, -1);
        font.drawString("Password:", ((sr.getScaledWidth() / 2f)-100)-font.getWidth("Password: "), height / 2f - 20, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        if(mouseX >= width / 2 - 100 && mouseX <= width / 2 + 100 && mouseY >= height / 2 + 25 && mouseY <= height / 2 + 45) {
            this.ssl.disableCertificateValidation();
            this.serviceSwitch.updateService(AlteningServiceType.MOJANG);

            System.out.println("Login button pressed");
            if(username.getText().isEmpty() || password.getText().isEmpty()) {
                loginText = "Please fill in all fields!";
                return;
            }
            loginText = "Logging in...";
            MicrosoftAccount account = LoginUtil.loginMicrosoft(username.getText(), password.getText());

            if(account == null) {
                loginText = "Login failed";
                return;
            }

            LoggerUtil.addTerminalMessage("Logged in as " + account.getProfile().getName());
            loginText = "Logged in as " + account.getSession().getUsername();
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {

    }
}
