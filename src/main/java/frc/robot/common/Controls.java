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
    public double SNAP_VALUE = -1;

    // Intake Controls
    public BooleanManager INTAKE = new BooleanManager();
    public BooleanManager OUTAKE = new BooleanManager();

    // Elevator Controls
    public BooleanManager STOW = new BooleanManager();
    public BooleanManager AMP_POSITION = new BooleanManager();
    public BooleanManager CLIMB_POSITION = new BooleanManager();
    public BooleanManager CLIMB_RAISE = new BooleanManager();
    public BooleanManager CLIMB_LOWER = new BooleanManager();

    // Shooter Controls
    public BooleanManager PRIME = new BooleanManager();
    public BooleanManager SCORE = new BooleanManager();
    public BooleanManager PODIUM_SHOT = new BooleanManager();
    public BooleanManager SUBWOOFER_SHOT = new BooleanManager();
    public BooleanManager PASS = new BooleanManager();

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

        SNAP_VALUE = driver.getPOV();
    }

    private void updateCommonControls() {
        updateDriveControls();
        INTAKE.update(driver.isPressing(XboxInput.LEFT_TRIGGER_BTN));
        SCORE.update(!driver.isPressing(XboxInput.START) && driver.isPressing(XboxInput.RIGHT_TRIGGER_BTN));
    }

    public void updateSingleDriver() {
        updateCommonControls();
        OUTAKE.update(driver.isPressing(XboxInput.DPAD_DOWN));

        STOW.update(driver.isPressing(XboxInput.A_BTN));
        AMP_POSITION.update(driver.isPressing(XboxInput.LEFT_BUMPER));
        CLIMB_POSITION.update(driver.isPressing(XboxInput.START));
        CLIMB_RAISE.update(driver.isPressing(XboxInput.DPAD_UP));
        CLIMB_LOWER.update(driver.isPressing(XboxInput.DPAD_DOWN));

        PRIME.update(driver.isPressing(XboxInput.RIGHT_BUMPER));
        PASS.update(driver.isPressing(XboxInput.B_BTN));

        SUBWOOFER_SHOT.update(driver.isPressing(XboxInput.B_BTN));
        PODIUM_SHOT.update(driver.isPressing(XboxInput.B_BTN));
    }

    public void updateDoubleDriver() {
        updateCommonControls();
        OUTAKE.update(driver.isPressing(XboxInput.DPAD_DOWN));

        STOW.update(manipulator.isPressing(XboxInput.A_BTN));
        AMP_POSITION.update(manipulator.isPressing(XboxInput.LEFT_BUMPER));
        CLIMB_POSITION.update(manipulator.isPressing(XboxInput.START));
        CLIMB_RAISE.update(manipulator.isPressing(XboxInput.DPAD_UP));
        CLIMB_LOWER.update(manipulator.isPressing(XboxInput.DPAD_DOWN));

        PRIME.update(manipulator.isPressing(XboxInput.RIGHT_BUMPER));
        PASS.update(!driver.isPressing(XboxInput.START) && driver.isPressing(XboxInput.RIGHT_BUMPER));

        SUBWOOFER_SHOT.update(driver.isPressing(XboxInput.START) && driver.isPressing(XboxInput.RIGHT_TRIGGER_BTN));
        PODIUM_SHOT.update(driver.isPressing(XboxInput.START) && driver.isPressing(XboxInput.RIGHT_BUMPER));
    }
}