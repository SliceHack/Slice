package slice.gui.alt;

import com.thealtening.AlteningServiceType;
import com.thealtening.SSLController;
import com.thealtening.TheAlteningAuthentication;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import slice.util.account.LoginUtil;
import the_fireplace.ias.gui.GuiPasswordField;

import java.io.IOException;

@Getter @Setter
@Deprecated
public class MojangAltLogin extends GuiScreen {

    private GuiTextField username;
    private GuiPasswordField password;

    private GuiScreen parent;

    private String loginMessage = "";
    private SSLController ssl = new SSLController();
    private TheAlteningAuthentication serviceSwitch = TheAlteningAuthentication.mojang();

    public MojangAltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = sr.getScaledWidth() / 2 - 100;
        int y = sr.getScaledHeight() / 2 - 100;
        this.username = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, x, y, 200, 20);
        this.password = new GuiPasswordField(1, Minecraft.getMinecraft().fontRendererObj, x, y + 25, 200, 20);
        this.buttonList.add(new GuiButton(2, x, y + 50, "Login"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        username.drawTextBox();
        password.drawTextBox();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        this.mc.fontRendererObj.drawString(loginMessage, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 - 100, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        if((mouseButton == 0 && mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100 && mouseY >= this.height / 2 - 100 && mouseY <= this.height / 2 + 100) && username.getText().length() > 0 && password.getText().length() > 0) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            this.ssl.disableCertificateValidation();
            this.serviceSwitch.updateService(AlteningServiceType.MOJANG);

            Session session = LoginUtil.loginMojang(username.getText(), password.getText());
            if (session == null) {
                loginMessage = "Invalid username or password";
                return;
            }

            loginMessage = "Logged in as " + session.getUsername();
        }
    }
}
