package lib.frc8592.controls;

import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.robot.Robot;
import frc.robot.common.Constants;

public class DriveScaler {
    private ScaleType type; // The type of scaling to apply
    private SlewRateLimiter slewLimiter; // Limits acceleration of inputs
    private boolean zeroAtDeadband = false; // The output starts at 0 at the deadband and not at x=0
    private double deadband = 0.06; // The point where the inputs start translating to outputs

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
        double activeDeadband = Robot.isSimulation() ? Constants.INPUT.SIMULATION_DEADBAND : deadband;

        if (Math.abs(input) > activeDeadband) {
            switch (type) {
                case QUADRATIC:
                case CUBIC:
                case LINEAR:
                
                    // All polynomial functions follow the same rule
                    if (zeroAtDeadband) {
                        scaledInput = (1 / Math.pow(1 - activeDeadband, type.ordinal() + 1)) * 
                                        Math.pow(input - activeDeadband, type.ordinal() + 1);
                    } else {
                        scaledInput = Math.pow(input, type.ordinal() + 1);
                    }
                    break;
                default:
                    // So far nothing
                    break;
            }
        }

        if (slewLimiter != null) {
            scaledInput = slewLimiter.calculate(scaledInput);
        }
        return scaledInput;
    }
}