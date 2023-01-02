package slice.gui.alt.field;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class GuiPasswordField extends GuiTextField {

    public GuiPasswordField(int id, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(id, fontrendererObj, x, y, par5Width, par6Height);
    }

    public void drawTextBox() {
        String prev = this.getText();
        this.text(StringUtils.repeat('*', prev.length()));
        super.drawTextBox();
        this.text(prev);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        String prev = this.getText();
        this.text(StringUtils.repeat('*', prev.length()));
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.text(prev);
    }

    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        return !GuiScreen.isKeyComboCtrlC(keyCode) && !GuiScreen.isKeyComboCtrlX(keyCode) && super.textboxKeyTyped(typedChar, keyCode);
    }

    public void text(String txt) {
        int cursorPosition = this.getCursorPosition();
        int selectionEnd = this.getSelectionEnd();
        this.setText(txt);
        this.setCursorPosition(cursorPosition);
        this.setSelectionPos(selectionEnd);
    }
}
