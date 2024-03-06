package frc.robot.controls;

import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.robot.common.Constants;

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
        if (limitSlewRate) slewLimiter = new SlewRateLimiter(Constants.SWERVE.SLEW_LIMIT);
    }

    /**
     * Scales the input by the desired scaling method
     */
    public double scale(double input) {
        double scaledInput = Math.pow(input, type.ordinal() + 1); // Currently only works with polynomial functions (x, x^2, x^3)
        if (slewLimiter != null) {
            scaledInput = slewLimiter.calculate(scaledInput);
        }
        return scaledInput;
    }
}