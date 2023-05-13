package com.sliceclient.ultralight.view;

import com.sliceclient.ultralight.Page;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.EventGuiRender;

@Getter @Setter
public class ViewNoGui {


    @Getter
    protected final Page page;

    protected Minecraft mc = Minecraft.getMinecraft();

    protected View view;

    protected boolean initialized;
    protected int lastWidth, lastHeight;

    public ViewNoGui(Page page) {
        this.page = page;
        Slice.INSTANCE.getEventManager().register(this);
    }

    public void init() {
        this.view = new View();
        this.view.loadPage(this.page);
        this.initialized = true;
    }

    @EventInfo
    public void onGuiRender(EventGuiRender e) {
        if(!initialized) {
            init();
            return;
        }

        if (!mc.gameSettings.showDebugInfo && mc.theWorld != null) {
            this.view.render();

            if(this.lastWidth != Display.getWidth() || this.lastHeight != Display.getHeight()) {
                this.view.resize(Display.getWidth(), Display.getHeight());
            }

            this.lastWidth = Display.getWidth();
            this.lastHeight = Display.getHeight();
        }
    }

}
