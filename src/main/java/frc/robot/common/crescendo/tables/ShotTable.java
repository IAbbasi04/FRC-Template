package frc.robot.common.crescendo.tables;

import frc.robot.common.crescendo.ShotProfile;
import frc.robot.common.regressions.*;
import frc.robot.modules.ElevatorModule;

public abstract class ShotTable {
    protected Regression leftShooterRPMRegression;
    protected Regression rightShooterRPMRegression;
    protected Regression elevatorPivotRegression;
    protected Regression elevatorExtensionRegression;

    /**
     * Uses interpolation tables to get the right shot from desired distances
     */
    public ShotProfile getShotFromDistance(double distance) {
        if (distance == -1) { // Speaker tag not visible
            return new ShotProfile().shouldShoot(false);
        }

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

    /**
     * Shoots the note at whatever angle it currently is
     */
    public ShotProfile getStaticShot() {
        double shooterRPM = 4000;
        double extension = ElevatorModule.getInstance().getElevatorExtensionMeters();
        double pivot = ElevatorModule.getInstance().getPivotAngleDegrees();
        return new ShotProfile()
            .flywheel(shooterRPM, shooterRPM)
            .pivot(pivot)
            .extension(extension)
            .shouldShoot(true)
        ;
    }

    /**
     * Statically set shot from subwoofer range
     */
    public ShotProfile getSubwooferShot() {
        return getShotFromDistance(1.4);
    }

    /**
     * Statically set shot from podium range
     */
    public ShotProfile getPodiumShot() {
        return getShotFromDistance(2.83);
    }
}