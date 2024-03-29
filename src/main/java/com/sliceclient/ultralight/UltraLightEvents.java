package com.sliceclient.ultralight;

import com.sliceclient.ultralight.view.View;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventGuiRender;
import slice.event.events.EventKey;
import slice.ultralight.ViewClickGui;
import slice.ultralight.ViewMainMenu;
import slice.util.Timer;

public class UltraLightEvents {

    @Getter
    private final UltraLightEngine ultraLightEngine;

    @Getter
    private ViewClickGui viewClickGui;

    private ViewMainMenu viewMainMenu;

    @Getter
    private final Timer timer = new Timer();

    @Getter
    private double width = 0, height = 0, scaledWidth = 0, scaledHeight = 0;

    @Getter
    private int scaledFactor = 1;

    public UltraLightEvents(UltraLightEngine ultraLightEngine) {
        this.ultraLightEngine = ultraLightEngine;
        Slice.INSTANCE.getEventManager().register(this);
    }

    public void init() {
        this.viewClickGui = new ViewClickGui();
    }

    @EventInfo
    public void onGuiRender(EventGuiRender e) {
        boolean resized = false;

        if(width != Display.getWidth()) {
            width = Display.getWidth();
            resized = true;
        }
        if(height != Display.getHeight()) {
            height = Display.getHeight();
            resized=true;
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        scaledWidth = sr.getScaledWidth();
        scaledHeight = sr.getScaledHeight();
        scaledFactor = sr.getScaleFactor();

        if(resized){
            ultraLightEngine.getViews().forEach((view) -> view.resize((long) width, (long) height));
        }

        ultraLightEngine.getRenderer().update();
        ultraLightEngine.getRenderer().render();

        if(timer.hasReached(500L)) {
            ultraLightEngine.getViews().forEach(View::gc);
            timer.reset();
        }
    }

    @EventInfo
    public void onKey(EventKey e) {
        int key = e.getKey();
        Minecraft mc = Minecraft.getMinecraft();

        if(key == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(viewClickGui);
        }
    }

    public ViewMainMenu getViewMainMenu() {

        if(this.viewMainMenu == null) {
            this.viewMainMenu = new ViewMainMenu();
        }

        return this.viewMainMenu;
    }

}