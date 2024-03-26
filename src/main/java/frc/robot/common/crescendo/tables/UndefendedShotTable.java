package frc.robot.common.crescendo.tables;

import frc.robot.common.regressions.LinearRegression;
import static frc.robot.common.Global.*;

public class UndefendedShotTable extends ShotTable {
    public UndefendedShotTable() {
        this.leftShooterRPMRegression = new LinearRegression(FLYHWEEL_RANGES[0], FLYHWEEL_RANGES[1]);
        this.rightShooterRPMRegression = new LinearRegression(FLYHWEEL_RANGES[0], FLYHWEEL_RANGES[2]);
        this.elevatorPivotRegression = new LinearRegression(UNDEFENDED_ELEVATOR_RANGES[0], UNDEFENDED_ELEVATOR_RANGES[1]);
        this.elevatorExtensionRegression = new LinearRegression(UNDEFENDED_ELEVATOR_RANGES[0], UNDEFENDED_ELEVATOR_RANGES[2]);
    }
}