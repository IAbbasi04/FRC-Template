package frc.robot.common;

import lib.frc8592.BooleanManager;
import lib.frc8592.controls.xbox.XboxController;
import lib.frc8592.controls.xbox.XboxInput;

public class Controls {
    private XboxController driver;
    private XboxController manipulator;

    // Drivetrain Controls
    public double SWERVE_TRANSLATE_X = 0.0;
    public double SWERVE_TRANSLATE_Y = 0.0;
    public double SWERVE_ROTATE = 0.0;
    public BooleanManager SNAIL_MODE = new BooleanManager();
    public BooleanManager RESET_GYRO = new BooleanManager();
    
    // General Controls
    public BooleanManager STOW = new BooleanManager();

    // Intake Controls
    public BooleanManager INTAKE = new BooleanManager();
    public BooleanManager PLACE = new BooleanManager();


    public void setControllers(XboxController driver, XboxController manipulator) {
        this.driver = driver;
        this.manipulator = manipulator;
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

    /**
     * Controls for just a singular driver
     */
    public void updateSingleDriver() {
        updateDriveControls();

        INTAKE.update(driver.isPressing(XboxInput.LEFT_TRIGGER_BTN));
        PLACE.update(driver.isPressing(XboxInput.DPAD_DOWN));
        STOW.update(driver.isPressing(XboxInput.A_BTN));
    }

    /**
     * Controls for 2 drivers
     */
    public void updateDoubleDriver() {
        updateDriveControls();

        INTAKE.update(manipulator.isPressing(XboxInput.LEFT_TRIGGER_BTN));
        PLACE.update(driver.isPressingAll(new XboxInput[]{XboxInput.START, XboxInput.LEFT_BUMPER}));
        STOW.update(driver.isPressing(XboxInput.A_BTN));
    }
}