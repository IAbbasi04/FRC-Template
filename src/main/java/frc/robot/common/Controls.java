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
    private BooleanManager MANUAL_MODE = new BooleanManager();
    public BooleanManager FORCE_STASH = new BooleanManager();

    // Note Controls
    public BooleanManager INTAKE = new BooleanManager();
    public BooleanManager SCORE = new BooleanManager();
    public BooleanManager FORCE_SCORE = new BooleanManager();
    public BooleanManager PRIME_SUBWOOFER = new BooleanManager();
    public BooleanManager PRIME_PODIUM = new BooleanManager();
    public BooleanManager AMP = new BooleanManager();
    public BooleanManager STAGE_AMP = new BooleanManager();
    public BooleanManager PLACE = new BooleanManager();
    public BooleanManager PASS = new BooleanManager();

    // Climber Controls
    public BooleanManager INITIATE_CLIMB = new BooleanManager();
    public BooleanManager RUN_CLIMB = new BooleanManager();

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
        SNAIL_MODE.update(driver.isPressing(XboxInput.LEFT_JOYSTICK_BTN));
        RESET_GYRO.update(driver.isPressing(XboxInput.START));
    }

    /**
     * Controls for just a singular driver
     */
    public void updateSingleDriver() {
        updateDriveControls();
        MANUAL_MODE.update(driver.isPressing(XboxInput.RIGHT_BUMPER)); // Change for single driver
        FORCE_STASH.update(driver.isPressing(XboxInput.A_BTN));

        INTAKE.update(driver.isPressing(XboxInput.LEFT_TRIGGER_BTN));
        SCORE.update(driver.isPressing(XboxInput.RIGHT_TRIGGER_BTN));
        FORCE_SCORE.update(driver.isPressing(XboxInput.DPAD_UP));

        PRIME_SUBWOOFER.update(MANUAL_MODE.getValue() && driver.isPressing(XboxInput.X_BTN));
        PRIME_PODIUM.update(MANUAL_MODE.getValue() && driver.isPressing(XboxInput.Y_BTN));

        AMP.update(driver.isPressing(XboxInput.LEFT_BUMPER));
        STAGE_AMP.update(driver.getReleased(XboxInput.LEFT_BUMPER));

        INITIATE_CLIMB.update(driver.getPressed(XboxInput.START));
        RUN_CLIMB.update(driver.isPressing(XboxInput.START));
        
        PASS.update(!MANUAL_MODE.getValue() && driver.isPressing(XboxInput.X_BTN));
        PLACE.update(driver.isPressing(XboxInput.DPAD_DOWN));
    }

    /**
     * Controls for 2 drivers
     */
    public void updateDoubleDriver() {
        updateDriveControls();
        MANUAL_MODE.update(manipulator.isPressing(XboxInput.START));
        FORCE_STASH.update(manipulator.isPressing(XboxInput.A_BTN));

        INTAKE.update(driver.isPressing(XboxInput.LEFT_TRIGGER_BTN));
        SCORE.update(driver.isPressing(XboxInput.RIGHT_TRIGGER_BTN));
        FORCE_SCORE.update(manipulator.isPressing(XboxInput.RIGHT_TRIGGER_BTN));

        PRIME_SUBWOOFER.update(manipulator.isPressing(XboxInput.RIGHT_BUMPER));
        PRIME_PODIUM.update(manipulator.isPressing(XboxInput.Y_BTN));

        AMP.update(manipulator.isPressing(XboxInput.LEFT_BUMPER));
        STAGE_AMP.update(manipulator.getReleased(XboxInput.LEFT_BUMPER));

        INITIATE_CLIMB.update(driver.getPressed(XboxInput.START));
        RUN_CLIMB.update(manipulator.isPressing(XboxInput.START));
        
        PASS.update(driver.isPressing(XboxInput.RIGHT_BUMPER));
        PLACE.update(manipulator.isPressing(XboxInput.DPAD_DOWN));
    }
}