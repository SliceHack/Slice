package slice.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AlteningAPI;
import com.thealtening.AlteningServiceType;
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
public class TheAlteningAPILogin extends GuiScreen {

    private GuiTextField token;
    private GuiButton login, generate;
    private String loginText, generateToken;

    private GuiScreen parent;

    public TheAlteningAPILogin(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
        this.token = new GuiTextField(height / 4 + 24, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.login = new GuiButton(0, width / 2 - 100, height / 2 + 20, 200, 20, "Login");
        this.generate = new GuiButton(1, width / 2 - 100, height / 2 + 40, 200, 20, "Generate Token");
        this.buttonList.add(this.login);
        this.buttonList.add(this.generate);
        loginText = null;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "TheAltening Login", width / 2, 20, 16777215);
        if(loginText != null) {
            TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 25);
            drawCenteredString(font, loginText, (float)width / 2, (float)height / 2, -1);
        }
        token.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(mouseX >= this.login.xPosition && mouseX <= this.login.xPosition + this.login.getButtonWidth() && mouseY >= this.login.yPosition && mouseY <= this.login.yPosition + this.login.getButtonWidth()) {
            login(this.token.getText());
        }
        if(mouseX >= this.generate.xPosition && mouseX <= this.generate.xPosition + this.generate.getButtonWidth() && mouseY >= this.generate.yPosition && mouseY <= this.generate.yPosition + this.generate.getButtonWidth()) {
            String token = AlteningAPI.generateAlteningFree();

            if(token == null) {
                loginText = "Failed to generate token";
                return;
            }

            if(!token.contains("@alt.com")) {
                loginText = token.replace("_", " ");
                return;
            }

            login(token);
        }
    }

    public void login(String t) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(t);
        auth.setPassword(t);

        try {
            auth.logIn();
            Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (final AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
        }

        if (auth.isLoggedIn()) {
            loginText = "Logged in as " + auth.getSelectedProfile().getName();
        }
    }
    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Button pressed: " + button.id);
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
