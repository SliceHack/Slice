package slice.gui.alt;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.*;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import slice.gui.alt.field.GuiPasswordField;
import slice.util.LoggerUtil;
import slice.util.account.LoginUtil;
import slice.util.account.microsoft.MicrosoftAccount;

import java.io.IOException;

@Getter @Setter
@Deprecated
public class MicrosoftAltLogin extends GuiScreen {

    private final GuiScreen parent;

    private GuiTextField username;
    private GuiPasswordField password;
    private String loginText;

    public MicrosoftAltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(0, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 4 + 96 + 12, "Login"));
        username = new GuiTextField(1, this.fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, 60, 200, 20);
        password = new GuiPasswordField(2, this.fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, 100, 200, 20);
        username.setFocused(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        drawCenteredString(this.fontRendererObj, "Microsoft Login", scaledResolution.getScaledWidth() / 2, 20, -1);
        drawCenteredString(this.fontRendererObj, "Username", scaledResolution.getScaledWidth() / 2 - 77, 47, -1);
        drawCenteredString(this.fontRendererObj, "Password", scaledResolution.getScaledWidth() / 2 - 77, 87, -1);

        if(loginText != null) drawCenteredString(this.fontRendererObj, loginText, scaledResolution.getScaledWidth() / 2, 30, -1);

        username.drawTextBox();
        password.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(parent);
        if(keyCode == Keyboard.KEY_RETURN) {
            if(username.isFocused()) {
                password.setFocused(true);
                username.setFocused(false);
            } else {
                login();
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    public void login() {
        new Thread(() -> {
            if(username.getText().isEmpty() || password.getText().isEmpty()) {

                if(!username.getText().isEmpty()) {
                    Session session = LoginUtil.loginOffline(username.getText());
                    loginText = "logged in as " + session.getUsername();
                    return;
                }

                loginText = "Please fill in all fields!";
                return;
            }

            loginText = "Logging in...";
            MicrosoftAccount account = LoginUtil.loginMicrosoft(username.getText(), password.getText());

            if(account == null) {
                loginText = "Login failed";
                return;
            }

            loginText = "Logged in as " + account.getSession().getUsername();
        }).start();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) login();
        super.actionPerformed(button);
    }
}
