package frc.robot.common.regressions;

import java.util.Arrays;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PiecewiseRegression extends Regression {
    private double[] values;
    private double deltaDistance; // meters
    public PiecewiseRegression(double[] values, double deltaDistanceMeters) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Input length does not match output length");
        }

        this.values = values;
        this.deltaDistance = deltaDistanceMeters;
    }

    @Override
    public double grabInterpolation(double input) {
        int startIndex = (int)(input/deltaDistance);
        int endIndex = startIndex + 1;

        SmartDashboard.putNumber("AAAAAAA", input);
        SmartDashboard.putNumber("BBAAAAA", startIndex);
        SmartDashboard.putNumber("EEAAAAA", endIndex);

        if (startIndex >= values.length - 1) { // Above possible range
            return values[values.length - 1];
        } else if (startIndex < 0) { // Below possible range
            return values[0];
        }

        double remainder = (input - startIndex*deltaDistance);// / deltaDistance;

        SmartDashboard.putNumber("CCCAAAAA", remainder);

        double additional = remainder/deltaDistance*(values[endIndex] - values[startIndex]);

        SmartDashboard.putNumber("DDDAAAAA", additional);
        
        SmartDashboard.putNumber("FFFFAAAAA", this.values[startIndex]);
        SmartDashboard.putNumber("GGGGAAAAA", this.values[endIndex]);

        SmartDashboard.putString("ABCDEFG", Arrays.toString(this.values));

        return values[startIndex] + additional;
    }

    @Override
    public double getResidual() {
        return 1.0; // Can't necessarily have residuals
    }

    @Override
    public String toString() {
        return "Not a typical regression model";
    }
}