package frc.robot.modes;

import frc.robot.controls.XboxController;

public abstract class ModeManager {
    protected XboxController driverController, operatorController; // Physical controllers used by the drivers

    /**
     * Passes in the driver and operator {@code XboxControllers} to all mode managers
     */
    public void setControllers(XboxController driver, XboxController operator) {
        this.driverController = driver;
        this.operatorController = operator;
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();
}