package frc.robot.common.crescendo.tables;

import frc.robot.common.crescendo.ShotProfile;
import frc.robot.common.regressions.*;

public abstract class ShotTable {
    protected Regression leftShooterRPMRegression;
    protected Regression rightShooterRPMRegression;
    protected Regression elevatorPivotRegression;
    protected Regression elevatorExtensionRegression;

    public ShotProfile getShotFromDistance(double distance) {
        if (distance == -1) { // Speaker tag not visible
            return new ShotProfile().shouldShoot(false);
        }

        double rightRPM = rightShooterRPMRegression.grabInterpolation(distance);
        double extension = elevatorExtensionRegression.grabInterpolation(distance);
        double leftRPM = leftShooterRPMRegression.grabInterpolation(distance);
        double pivot = elevatorPivotRegression.grabInterpolation(distance);

        return new ShotProfile()
            .flywheel(leftRPM, rightRPM)
            .pivot(pivot)
            .extension(extension)
            .shouldShoot(true)
        ;
    }
}