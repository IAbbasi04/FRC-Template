package frc.robot.common.crescendo.tables;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.common.regressions.LinearRegression;

public abstract class ShotTable {
    protected LinearRegression leftShooterRPMRegression;
    protected LinearRegression rightShooterRPMRegression;
    protected LinearRegression elevatorPivotRegression;
    protected LinearRegression elevatorExtensionRegression;

    public ShotProfile getShotFromDistance(double distance) {
        if (distance == -1) { // Speaker tag not visible
            return new ShotProfile().shouldShoot(false);
        }

        SmartDashboard.putNumber("AAAAAJKDSAKLJASD", leftShooterRPMRegression.grabInterpolation(distance));

        double leftRPM = leftShooterRPMRegression.grabInterpolation(distance);
        double rightRPM = rightShooterRPMRegression.grabInterpolation(distance);
        double pivot = elevatorPivotRegression.grabInterpolation(distance);
        double extension = elevatorExtensionRegression.grabInterpolation(distance);

        return new ShotProfile()
            .flywheel(leftRPM, rightRPM)
            .pivot(pivot)
            .extension(extension)
            .shouldShoot(true)
        ;
    }
}