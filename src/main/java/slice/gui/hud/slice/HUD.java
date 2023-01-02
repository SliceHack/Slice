package slice.gui.hud.slice;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import slice.Slice;
import slice.event.data.EventInfo;
import slice.event.events.Event2D;
import slice.event.events.EventClientTick;
import slice.font.TTFFontRenderer;
import slice.gui.hud.legacy.arraylist.ArrayListHUD;
import slice.module.Module;
import slice.util.GradientUtil;
import slice.util.LoggerUtil;
import slice.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The hud class
 *
 * @author Nick
 * */
@SuppressWarnings("unused")
@Deprecated
public class HUD {

    private int ticks;

    @EventInfo
    public void onTick(EventClientTick e) {
        if(ticks >= 20) ticks = 0;
        ticks++;
    }

    @EventInfo
    public void onLogo(Event2D e) {
        TTFFontRenderer ttf = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 60);
        RenderUtil.drawRoundedRect(5, 5, ttf.getWidth("Slice")+5, ttf.getHeight("Slice")+5, 15, new Color(0, 0, 0, 155).getRGB());

        int color = GradientUtil.gradient(Color.RED.getRGB(), Color.RED.darker().getRGB());
        LoggerUtil.addMessage("Gradient: " + color);
        ttf.drawStringWithShadow("Slice", 5, 5, color);
    }

    @EventInfo
    public void onArrayList(Event2D e) {
        TTFFontRenderer ttf = Slice.INSTANCE.getFontManager().getFont("Poppins-Regular", 20);
        List<Module> moduleNames = ArrayListHUD.getEnabledModules(ttf);
        int y = 0;
        for(Module module : moduleNames) {
            String s = module.getMode() == null ? module.getName() : module.getName() + " §7" + module.getMode().getValue();

            RenderUtil.drawRoundedRect(e.getWidth() - ttf.getWidth(s) - 3, y, (ttf.getWidth(s) + 5)+(e.getWidth() - ttf.getWidth(s) - 2), (ttf.getHeight(s) + 5)+y, 0, new Color(0, 0, 0, 155).getRGB());

            ttf.drawStringWithShadow(s, e.getWidth() - ttf.getWidth(s), y, -1);
            y += ttf.getHeight(s) + 5.5;
        }
    }

}
