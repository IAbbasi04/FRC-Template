package frc.robot.common.regressions;

public abstract class Regression {
    /**
     * Returns y assuming input is x
     */
    public abstract double grabLinearInterpolation(double input);

    public abstract String toString();

    /**
     * Predicts the regression type based on residuals
     */
    public double get(double[] inputs, double[] outputs) {
        if (inputs.length != outputs.length) {
            throw new IllegalArgumentException("Input length does not match output length");
        } else if (inputs.length == 0) {
            throw new IllegalArgumentException("Not enough values");
        }
        
        return 0.0;
    }
}