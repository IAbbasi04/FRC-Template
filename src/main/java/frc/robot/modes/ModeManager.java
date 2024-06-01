package frc.robot.modes;

import lib.frc8592.controls.xbox.XboxController;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.subsystems.*;
import frc.robot.subsystems.LEDSubsystem.LEDMode;

public abstract class ModeManager {
    // Physical controllers used by the drivers
    protected XboxController driverController = new XboxController(Constants.INPUT.DRIVER_CONTROLLER_PORT);
    protected XboxController manipulatorController = new XboxController(Constants.INPUT.MANIPULATOR_CONTROLLER_PORT);

    /**
     * Passes controllers into the controls class for use in various match modes
     */
    public void activateControllers() {
        Robot.CONTROLS.setControllers(driverController, manipulatorController);
    }

    /**
     * Updates inputs for the leds the entire match mode
     */
    protected void updateLED() {
        LEDMode desiredLEDMode = LEDMode.kOff;
        LEDSubsystem.getInstance().setLEDMode(desiredLEDMode);
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();
}