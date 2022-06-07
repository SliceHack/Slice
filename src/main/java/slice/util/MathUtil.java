package slice.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class to help with math.
 *
 * @author Nick
 * */
@UtilityClass
public class MathUtil {

    /**
     * Rounds a double to a certain number of decimal places.
     * @param value - the value to round
     * @param places - the number of decimal places to round to
     * */
    public double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
