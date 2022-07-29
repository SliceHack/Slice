package slice.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static slice.util.ColorUtil.getRGB;

/**
 * Utility class for creating gradients.
 *
 * @author Slice
 */
@UtilityClass
public class GradientUtil {

    public static int gradient(int col1, int col2) {
        float[] hsb1 = getRGB(col1);
        float[] hsb2 = getRGB(col2);
        float[] hsb = new float[3];
        hsb[0] = (hsb1[0] + hsb2[0]) / 2;
        hsb[1] = (hsb1[1] + hsb2[1]) / 2;
        hsb[2] = (hsb1[2] + hsb2[2]) / 2;

        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * Interpolates between two colors.
     * @param col1 The first color.
     * @param col2 The second color.
     * @param percent The percent of the way between the two colors.
     * @return The interpolated color.
     */
    public static int interpolate(int col1, int col2, float percent) {
        float mult = percent / 100;
        int r = (int) (((col1 >> 16) & 0xFF) * (1 - mult) + ((col2 >> 16) & 0xFF) * mult);
        int g = (int) (((col1 >> 8) & 0xFF) * (1 - mult) + ((col2 >> 8) & 0xFF) * mult);
        int b = (int) ((col1 & 0xFF) * (1 - mult) + (col2 & 0xFF) * mult);
        return (r << 16) | (g << 8) | b;
    }
}
