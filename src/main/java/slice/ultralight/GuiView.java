package slice.ultralight;

import com.labymedia.ultralight.input.*;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

/***
 * for html rendering we need ultralight
 * this code is not written by us, we converted it from kotlin to java
 *
 * @author CCBlueX || @UnlegitMC
 * */
@Getter @Setter
public class GuiView extends GuiScreen {
    private View view;

    private UltraLightEngine engine;

    public GuiView(UltraLightEngine engine, String url) {
        this.engine = engine;
        this.view = new View(engine);
        this.view.loadURL(url);
        this.engine.registerView(view);
        this.view.loadURL(url);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // mouse stroll
        if (Mouse.hasWheel()) {
            val wheel = Mouse.getDWheel();
            if (wheel != 0) {
                view.fireScrollEvent(new UltralightScrollEvent()
                        .deltaX(0)
                        .deltaY(wheel)
                        .type(UltralightScrollEventType.BY_PIXEL));
            }
        }

        // mouse move
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.MOVED)
                .x(mouseX * engine.getFactor())
                .y(mouseY * engine.getFactor())
                .button(UltralightMouseEventButton.LEFT));

        view.render();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        UltralightMouseEventButton button = getButtonByButtonID(mouseButton);
        if(button == null) return;
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.DOWN)
                .x(mouseX * engine.getFactor())
                .y(mouseY * engine.getFactor())
                .button(button));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int key) {
        UltralightMouseEventButton button = getButtonByButtonID(key);
        if(button == null) return;
        view.fireMouseEvent(new UltralightMouseEvent()
                .type(UltralightMouseEventType.UP)
                .x(mouseX * engine.getFactor())
                .y(mouseY * engine.getFactor())
                .button(button));
    }

    @Override
    public void onGuiClosed() {
        engine.unregisterView(view);
        super.onGuiClosed();
    }

    public UltralightMouseEventButton getButtonByButtonID(int key) {
        switch (key) {
            case 0: return UltralightMouseEventButton.LEFT;
            case 1: return UltralightMouseEventButton.MIDDLE;
            case 2: return UltralightMouseEventButton.RIGHT;
            default: return null;
        }
    }
}
