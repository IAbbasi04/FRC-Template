package frc.robot.common.regressions;

import frc.robot.common.Utils;

public class LinearRegression extends Regression {
    private double slope, intercept;

    public LinearRegression(double[] inputs, double[] outputs) {
        if (inputs.length != outputs.length) {
            throw new IllegalArgumentException("Input length does not match output length");
        } else if (inputs.length == 0) {
            throw new IllegalArgumentException("Not enough values");
        }
        
        double xAvg = 0, yAvg = 0;
        for (int count = 0; count < inputs.length; count++) {
            xAvg += inputs[count] / inputs.length;
            yAvg += outputs[count] / inputs.length;
        }

        double slope = 0;
        double dev2 = 0;
        for (int count = 0; count < inputs.length; count++) {
            double xDev = (inputs[count] - xAvg);
            double yDev = (outputs[count] - yAvg);
            dev2 += xDev * xDev;
            slope += xDev * yDev;
        }

        slope /= dev2;

        double intercept = yAvg - slope * xAvg;

        this.slope = slope;
        this.intercept = intercept;
    }

    @Override
    public double grabLinearInterpolation(double input) {
        return this.slope * input + this.intercept;
    }

    public double grabInverseInterpolation(double output) {
        return -(output - this.intercept) / this.slope;
    }

    @Override
    public String toString() {
        return "y = " + Utils.roundTo(slope, 3) + "x + " + Utils.roundTo(intercept, 3);
    }
}