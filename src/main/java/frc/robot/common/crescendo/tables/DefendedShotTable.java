package frc.robot.common.crescendo.tables;

import static frc.robot.common.crescendo.Global.*;
import lib.frc8592.regression.*;

public class DefendedShotTable extends ShotTable {
    public DefendedShotTable() {
        this.leftShooterRPMRegression = new LinearRegression(FLYHWEEL_RANGES[0], FLYHWEEL_RANGES[1]);
        this.rightShooterRPMRegression = new LinearRegression(FLYHWEEL_RANGES[0], FLYHWEEL_RANGES[2]);
        this.elevatorPivotRegression = new LinearRegression(DEFENDED_ELEVATOR_RANGES[0], DEFENDED_ELEVATOR_RANGES[1]);
        this.elevatorExtensionRegression = new LinearRegression(DEFENDED_ELEVATOR_RANGES[0], DEFENDED_ELEVATOR_RANGES[2]);
    }
}