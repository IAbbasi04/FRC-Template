package lib.frc8592.controls;

import edu.wpi.first.math.filter.SlewRateLimiter;

public class DriveScaler {
    private ScaleType type; // The type of scaling to apply
    private SlewRateLimiter slewLimiter; // Limits acceleration of inputs
    private boolean zeroAtDeadband = false; // The output starts at 0 at the deadband and not at x=0
    private double deadband = 0.05; // The point where the inputs start translating to outputs

    public enum ScaleType {
        LINEAR,
        QUADRATIC,
        CUBIC
    }

    public DriveScaler(ScaleType type, boolean limitSlewRate, boolean zeroAtDeadband) {
        this.type = type;
        this.zeroAtDeadband = zeroAtDeadband;
        if (limitSlewRate) slewLimiter = new SlewRateLimiter(2.0); // 200% change per second
    }

    /**
     * Scales the input by the desired scaling method
     */
    public double scale(double input) {
        double scaledInput = 0.0;
        switch (type) {
            case LINEAR:
            case QUADRATIC:
            case CUBIC:
                // All polynomial functions follow the same rule
                if (zeroAtDeadband) {
                    scaledInput = (1 / Math.pow(1 - deadband, type.ordinal() + 1)) * 
                                    Math.pow(input - deadband, type.ordinal() + 1);
                } else {
                    scaledInput = Math.pow(input, type.ordinal() + 1);
                }
                break;
            default:
                // So far nothing
                break;
        }

        if (slewLimiter != null) {
            scaledInput = slewLimiter.calculate(scaledInput);
        }
        return scaledInput;
    }
}