package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Class for rendering stuff
 *
 * @author Nick
 * */
@UtilityClass
public class RenderUtil {

    public static void drawRoundedRect(int x, int y, int width, int height, int color) {
        drawRoundedRect(x, y, x + width, y + width, 10, color);
    }

    /* Did not write this method (to much math) */
    public static void drawRoundedRect(double left, double top, double right, double bottom, double radius, int color) {
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        left *= 2.0D;
        top *= 2.0D;
        right *= 2.0D;
        bottom *= 2.0D;
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GlStateManager.enableBlend();
        glColor(color);
        GL11.glBegin(9);
        int i;
        for (i = 0; i <= 90; i++)
            GL11.glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, top + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i++)
            GL11.glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i++)
            GL11.glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i++)
            GL11.glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, top + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }
}