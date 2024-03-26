package frc.robot.modes;

import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.controls.XboxController;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.LEDModule;
import frc.robot.modules.ShooterModule;
import frc.robot.modules.LEDModule.LEDMode;
import frc.robot.modules.FeederModule.FeederState;

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
     * Updates inputs for the leds the entire match mode
     */
    protected void updateLED() {
        LEDMode desiredLEDMode = LEDMode.kOff;
        switch (FeederModule.getInstance().getNoteState()) {
            case kStaged:
                desiredLEDMode = LEDMode.kStagedNote;
                break;
            case kHasNote: // Fall through intentional for now
            case kAligning:
                desiredLEDMode = LEDMode.kHasNote;
                break;
            case kNone:
                desiredLEDMode = LEDMode.kOff;
                break;
            default:
                break;
        }

        LEDModule.getInstance().setLEDMode(desiredLEDMode);
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();

    /**
     * Intakes a note
     */
    public static void setIntaking() {
        IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
        FeederModule.getInstance().setFeederState(FeederState.kIntake);
    }

    /**
     * Powers off intake and feeder
     */
    public static void stopAllRollers() {
        IntakeModule.getInstance().setRollerVelocity(0.0);
        FeederModule.getInstance().setFeederState(FeederState.kOff);
    }

    /**
     * Spit out a note from the front
     */
    public static void setOutaking() {
        IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTTAKE_RPM);
        FeederModule.getInstance().setFeederState(FeederState.kOutake);
    }

    /**
     * Shoots the note
     */
    public static void setShooting(double leftRPM, double rightRPM, double pivotAngle, double elevatorHeight) {
        ShooterModule.getInstance().setDesiredRPM(leftRPM, rightRPM);
        if (ShooterModule.getInstance().atTargetVelocity()) {
            FeederModule.getInstance().setFeederState(FeederState.kShoot);
        }
    }

    /**
     * Shoots the note based on a shot profile
     */
    public static void setShooting(ShotProfile shotProfile) {
        ModeManager.setShooting(
            shotProfile.leftFlywheelRPM, 
            shotProfile.rightFlywheelRPM, 
            shotProfile.elevatorPivotDegrees, 
            shotProfile.elevatorExtensionMeters
        );
    }

    /**
     * Sets the velocity of the shooter motors
     */
    public static void setShooterVelocity(double leftRPM, double rightRPM) {
        ShooterModule.getInstance().setDesiredRPM(leftRPM, rightRPM);
    }

    /**
     * Turns off shooter motors
     */
    public static void stopShooter() {
        ShooterModule.getInstance().setDesiredRPM(0, 0);
    }

    /**
     * Sets the angle of the pivot
     */
    public static void setPivotAngle() {}

    /**
     * Sets the height of the elevator
     */
    public static void setElevatorHeight() {}
}