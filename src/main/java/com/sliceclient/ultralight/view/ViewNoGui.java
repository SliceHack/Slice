package com.sliceclient.ultralight.view;

import com.sliceclient.ultralight.Page;
import com.sliceclient.ultralight.UltraLightEngine;
import com.sliceclient.ultralight.js.SliceJsContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
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


    @Getter @Setter
    protected SliceJsContext context;

    public ViewNoGui(Page page) {
        this.page = page;
        Slice.INSTANCE.getEventManager().register(this);
    }

    public void init() {
        this.view = new View();
        this.view.loadPage(this.page);
        this.initialized = true;

        this.context = new SliceJsContext(this.view);
        this.context.loadContext(this.view, this.view.getView().lockJavascriptContext().getContext());

        UltraLightEngine.getInstance().registerView(this.view);
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
