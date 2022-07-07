package slice.gui.alt.manager.ui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import slice.Slice;
import slice.font.TTFFontRenderer;
import slice.util.RenderUtil;
import slice.util.account.LoginUtil;

import java.awt.*;
import java.util.Objects;

@Getter @Setter
public class AltButton extends GuiButton {

    private String alt;

    public AltButton(String alt, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.alt = alt;
    }

    public AltButton(String alt, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.alt = alt;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        TTFFontRenderer font = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", (height/5)*5);

        String name = null;
        try {
            try {
                name = this.alt.split(":")[2];
            } catch (Exception e) {
                name = this.alt.split(":")[0];
            }
        } catch (Exception ignored){}

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        RenderUtil.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + (font.getWidth(Objects.requireNonNull(name))+5), this.yPosition + 20, 15, 0xFF000000);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        font.drawCenteredString(name, this.xPosition + ((font.getWidth(Objects.requireNonNull(name))+5)/2), this.yPosition + (5), 0xFFFFFFFF);
        GlStateManager.popMatrix();
    }

    public void click() {
        try {
            String type = this.alt.split(":")[3];
            System.out.println(type);
            switch (type) {
                case "mojang":
                    Minecraft.getMinecraft().session = LoginUtil.loginMojang(this.alt.split(":")[0], this.alt.split(":")[1]);
                    break;
                case "microsoft":
                    Minecraft.getMinecraft().session = Objects.requireNonNull(LoginUtil.loginMicrosoft(this.alt.split(":")[0], this.alt.split(":")[1])).getSession();
                case "altening":
                    Minecraft.getMinecraft().session = LoginUtil.loginAlteining(this.alt.split(":")[0]);
                    break;
                case "offline":
                    Minecraft.getMinecraft().session = LoginUtil.loginOffline(this.alt.split(":")[0]);
                    break;
            }
        } catch (Exception ignored){}
    }
}
