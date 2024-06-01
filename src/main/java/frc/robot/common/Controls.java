package frc.robot.common;

import lib.frc8592.BooleanManager;
import lib.frc8592.controls.xbox.XboxController;
import lib.frc8592.controls.xbox.XboxInput;

public class Controls {
    private XboxController driver;
    // private XboxController manipulator;

    // Drivetrain Controls
    public double SWERVE_TRANSLATE_X = 0.0;
    public double SWERVE_TRANSLATE_Y = 0.0;
    public double SWERVE_ROTATE = 0.0;
    public BooleanManager SNAIL_MODE = new BooleanManager();
    public BooleanManager RESET_GYRO = new BooleanManager();

    // Intake Controls
    public static BooleanManager INTAKE = new BooleanManager();

    public void setControllers(XboxController driver, XboxController manipulator) {
        this.driver = driver;
        // this.manipulator = manipulator;
    }

    private void updateDriveControls() {
        // Since the drivetrain controls work consistently 
        // whether there is a single driver or multiple
        // it's convenient to have a seperate method for it
        SWERVE_TRANSLATE_X = -driver.get(XboxInput.LEFT_Y_AXIS);
        SWERVE_TRANSLATE_Y = -driver.get(XboxInput.LEFT_X_AXIS);
        SWERVE_ROTATE = -driver.get(XboxInput.RIGHT_X_AXIS);
        SNAIL_MODE.update(driver.isPressing(XboxInput.RIGHT_BUMPER));
        RESET_GYRO.update(driver.isPressing(XboxInput.START));
    }

    public void updateSingleDriver() {
        updateDriveControls();
    }

    public void updateDoubleDriver() {
        updateDriveControls();
    }
}