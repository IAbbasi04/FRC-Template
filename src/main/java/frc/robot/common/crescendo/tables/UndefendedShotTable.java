package frc.robot.common.crescendo.tables;

import static frc.robot.common.crescendo.Global.*;
import lib.frc8592.regression.*;

public class UndefendedShotTable extends ShotTable {
    public UndefendedShotTable() {
        this.leftShooterRPMRegression = new PiecewiseRegression(FLYHWEEL_RANGES[1], 0.2);
        this.rightShooterRPMRegression = new PiecewiseRegression(FLYHWEEL_RANGES[2], 0.2);
        this.elevatorPivotRegression = new PiecewiseRegression(UNDEFENDED_ELEVATOR_RANGES[1], 0.2);
        this.elevatorExtensionRegression = new PiecewiseRegression(UNDEFENDED_ELEVATOR_RANGES[2], 0.2);
    }
}