package lib.frc8592.controls;

import edu.wpi.first.math.filter.SlewRateLimiter;

public class DriveScaler {
    private ScaleType type; // The type of scaling to apply
    private SlewRateLimiter slewLimiter; // Limits acceleration of inputs

    public enum ScaleType {
        LINEAR,
        QUADRATIC,
        CUBIC
    }

    public DriveScaler(ScaleType type, boolean limitSlewRate) {
        this.type = type;
        if (limitSlewRate) slewLimiter = new SlewRateLimiter(2.0); // 200% change per second
    }

    /**
     * Scales the input by the desired scaling method
     */
    public double scale(double input) {
        double scaledInput = Math.pow(input, type.ordinal() + 1); // Currently only works with basic polynomial functions (x, x^2, x^3)
        if (slewLimiter != null) {
            scaledInput = slewLimiter.calculate(scaledInput);
        }
        return scaledInput;
    }
}