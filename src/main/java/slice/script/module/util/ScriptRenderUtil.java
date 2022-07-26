package slice.script.module.util;

import slice.util.RenderUtil;

/**
 * Class for rendering stuff
 *
 * @author Nick
 * */
public class ScriptRenderUtil {

    /** instance */
    public static ScriptRenderUtil INSTANCE = new ScriptRenderUtil();

    /**
     * Render's an Image
     * */
    public void drawImage(String location, int x, int y, int width, int height) {
        RenderUtil.drawImage(location, x, y, width, height);
    }

    /**
     * @see #drawRoundedRect(double, double, double, double, double, int)
     */
    public void drawRoundedRect(int x, int y, int width, int height, int color) {
        RenderUtil.drawRoundedRect(x, y, x + width, y + width, 10, color);
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
    public void drawRect(int x, int y, int width, int height, int color) {
        RenderUtil.drawRect(x, y, x + width, y + height, color);
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
    public void drawRoundedRect(double left, double top, double right, double bottom, double radius, int color) {
        RenderUtil.drawRoundedRect(left, top, right, bottom, radius, color);
    }

    /**
     * Colors using the hexadecimal color format
     * @pamra hex the hexadecimal color
     */
    public void glColor(int hex) {
        RenderUtil.glColor(hex);
    }
}
