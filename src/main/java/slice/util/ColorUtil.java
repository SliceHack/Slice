package slice.util;

import lombok.experimental.UtilityClass;

import java.awt.*;

/**
 * Color utility class.
 *
 * @author Nick
 **/
@UtilityClass
public class ColorUtil {

    /**
     * Returns a color with the specified RGB values.
     *
     * @param seconds the seconds
     * @param saturation the saturation
     * @param brightness the brightness
     * @return the color
     */
    public static int getRainbow(float seconds, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (seconds * 1000) / (seconds * 1000));
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    /**
     * Gets a rainbow wave color.
     * @param seconds The number of seconds in the rainbow.
     * @param saturation The saturation of the color.
     * @param brightness The brightness of the color.
     * @parma index The index of the color.
     * @return The color.
     * */
    public static int getRainbow(float seconds, float saturation, float brightness, long index) {
        float hue = ((System.currentTimeMillis()+index) % (seconds * 1000) / (seconds * 1000));
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static float[] getRGB(int color) {
        float[] hsb = new float[3];
        Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsb);
        return hsb;
    }

    public static int getRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static int getRGB(float i, float i1, float i2, float i3) {
        return (int) (i * 255) << 24 | (int) (i1 * 255) << 16 | (int) (i2 * 255) << 8 | (int) (i3 * 255);
    }
}
