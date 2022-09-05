package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.UUID;

/**
 * Class for rendering stuff
 *
 * @author Nick
 * */
@UtilityClass
public class RenderUtil {

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

    /**
     * Render's an Image
     * */
    public static void drawImage(String location, int x, int y, int width, int height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceUtil.getResource(location));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    }

    /**
     * @see #drawRoundedRect(double, double, double, double, double, int)
     */
    public static void drawRoundedRect(int x, int y, int width, int height, int color) {
        drawRoundedRect(x, y, x + width, y + width, 10, color);
    }

    public static void drawHead(UUID uuid, int x, int y, int width, int height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getNetHandler().getPlayerInfo(uuid).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,64F,64F);
    }

    /**
     * Draws a rectangle rectangle
     *
     * @param x The x coordinate of the rectangle
     * @param y The y coordinate of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param color The color of the rectangle
     * */
    public static void drawRect(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    /**
     * draws a rounded rectangle
     * @parma left the left side of the rectangle
     * @param top the top side of the rectangle
     * @param right the right side of the rectangle
     * @param bottom the bottom side of the rectangle
     * @param radius the radius of the corners
     * @param color the color of the rectangle
     * */
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

        // math to round the rectangle
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

    /**
     * Colors using the hexadecimal color format
     * @pamra hex the hexadecimal color
     */
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    /**
     * draws a bounding box
     *
     * @pamra axisAlignedBB the axis
     * @pamra color the color
     * */
    public void drawBoundingBox(AxisAlignedBB axisAlignedBB, Color color) {
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        glColor(color.getRGB());
        boundingPox(axisAlignedBB);
    }

    /***
     * Outlines a bounding box
     *
     * @param axisAlignedBB the axis
     * @param color the color
     *
     * */
    public void outlineBoundingBox(AxisAlignedBB axisAlignedBB, Color color) {
        worldRenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        glColor(color.getRGB());
        boundingPox(axisAlignedBB);
    }

    private static void boundingPox(AxisAlignedBB axisAlignedBB) {
        RenderUtil.worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        RenderUtil.worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        RenderUtil.worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        RenderUtil.worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        RenderUtil.worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        RenderUtil.tessellator.draw();
    }

    public void glColor(Color color) {
        GL11.glColor4d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    /**
     * This Method prevents memory leaks
     * */
    public void setupFBO() {
        final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();

        if (fbo != null) {
            if (fbo.depthBuffer > -1) {
                final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
                EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
                EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
                EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
                fbo.depthBuffer = -1;
            }
        }
    }
}
