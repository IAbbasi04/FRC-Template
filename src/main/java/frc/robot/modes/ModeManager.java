package frc.robot.modes;

import org.frc8592.controls.xbox.XController;
import frc.robot.modules.*;
import frc.robot.modules.LEDModule.LEDMode;

public abstract class ModeManager {
    protected XController driverController, operatorController; // Physical controllers used by the drivers

    /**
     * Passes in the driver and operator {@code XController} to all mode managers
     */
    public void setControllers(XController driver, XController operator) {
        this.driverController = driver;
        this.operatorController = operator;
    }

    /**
     * Updates inputs for the leds the entire match mode
     */
    protected void updateLED() {
        LEDMode desiredLEDMode = LEDMode.kOff;
        LEDModule.getInstance().setLEDMode(desiredLEDMode);
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();
}