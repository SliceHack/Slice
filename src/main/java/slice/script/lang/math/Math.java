package slice.script.lang.math;

import lombok.Data;
import net.minecraft.util.MathHelper;

/**
 * Math class for the scripting language.
 *
 * @author Dylan
*/
@Data @SuppressWarnings("unused")
public class Math {
    public static Math INSTANCE = new Math();

    public double PI = java.lang.Math.PI;
    public double E = java.lang.Math.E;

    public float sin(float input) {
        return MathHelper.sin(input);
    }

    public float cos(float input) {
        return MathHelper.cos(input);
    }

    public double tan(float input) {
        return java.lang.Math.tan(input);
    }

    public double round(double input) {
        return java.lang.Math.round(input);
    }

    public double floor(double input) {
        return java.lang.Math.floor(input);
    }

    public double ceil(double input) {
        return java.lang.Math.ceil(input);
    }

    public double abs(double input) {
        return java.lang.Math.abs(input);
    }

    public double sqrt(double input) {
        return java.lang.Math.sqrt(input);
    }

    public double pow(double input, double power) {
        return java.lang.Math.pow(input, power);
    }

    public double log(double input) {
        return java.lang.Math.log(input);
    }

    public double log10(double input) {
        return java.lang.Math.log10(input);
    }

    public double exp(double input) {
        return java.lang.Math.exp(input);
    }

    public double sinh(double input) {
        return java.lang.Math.sinh(input);
    }

    public double cosh(double input) {
        return java.lang.Math.cosh(input);
    }

    public double tanh(double input) {
        return java.lang.Math.tanh(input);
    }

    public double asin(double input) {
        return java.lang.Math.asin(input);
    }

    public double acos(double input) {
        return java.lang.Math.acos(input);
    }

    public double atan(double input) {
        return java.lang.Math.atan(input);
    }

    public double atan2(double input1, double input2) {
        return java.lang.Math.atan2(input1, input2);
    }

    public double hypot(double input1, double input2) {
        return java.lang.Math.hypot(input1, input2);
    }

    public double cbrt(double input) {
        return java.lang.Math.cbrt(input);
    }

    public double square(double input) {
        return input * input;
    }

    public double cube(double input) {
        return input * input * input;
    }

    public double min(double input1, double input2) {
        return java.lang.Math.min(input1, input2);
    }

    public double max(double input1, double input2) {
        return java.lang.Math.max(input1, input2);
    }

    public double clamp(double input, double min, double max) {
        return java.lang.Math.max(min, java.lang.Math.min(input, max));
    }

    public double random() {
        return java.lang.Math.random();
    }

    public double random(double max) {
        return java.lang.Math.random() * max;
    }

    public double random(double min, double max) {
        return java.lang.Math.random() * (max - min) + min;
    }

    public double random(double min, double max, double step) {
        return java.lang.Math.floor((java.lang.Math.random() * (max - min) + min) / step) * step;
    }

    public double mod(double input, double mod) {
        return java.lang.Math.floor(input / mod) * mod;
    }

    

    public float toDegrees(float input) {
        return (float) (input * (180 / this.PI));
    }

    public float toRadians(float input) {
        return (float) (input * (this.PI / 180));
    }

}
