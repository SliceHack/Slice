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

}
