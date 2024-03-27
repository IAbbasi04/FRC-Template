package frc.robot.modes;

import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.controls.XboxController;
import frc.robot.modules.ElevatorModule;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.LEDModule;
import frc.robot.modules.ShooterModule;
import frc.robot.modules.ElevatorModule.ElevatorState;
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
                desiredLEDMode = LEDMode.kDisabled;
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
     * Powers off intake
     */
    public static void stopIntake() {
        IntakeModule.getInstance().setRollerVelocity(0.0);
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
    public static void setShooting(double leftRPM, double rightRPM, double pivotAngle, double elevatorHeight, boolean shouldShoot) {
        ShooterModule.getInstance().setDesiredRPM(leftRPM, rightRPM);
        ElevatorModule.getInstance().setPivot(pivotAngle);
        ElevatorModule.getInstance().setExtension(elevatorHeight);
        if (ShooterModule.getInstance().atTargetVelocity() && 
            ElevatorModule.getInstance().atTargetPosition() &&
            shouldShoot || true) {
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
            shotProfile.elevatorExtensionMeters,
            shotProfile.shouldShoot
        );
    }

    /**
     * Sets the velocity of the shooter motors
     */
    public static void setShooterVelocity(double leftRPM, double rightRPM) {
        ShooterModule.getInstance().setDesiredRPM(leftRPM, rightRPM);
    }

    /**
     * Shooter and feeder presets for scoring in amp
     */
    public static void scoreAmp() {
        setShooterVelocity(-1000, -1000);
        FeederModule.getInstance().setFeederVelocity(-3000);
    }

    /**
     * Turns off shooter motors
     */
    public static void stopShooter() {
        ShooterModule.getInstance().setDesiredRPM(0, 0);
    }

    /**
     * Turns off feeder motor
     */
    public static void stopFeeder() {
        FeederModule.getInstance().setFeederState(FeederState.kOff);
    }

    /**
     * Sets the angle of the pivot
     */
    public static void setPivotAngle(double pivot) {
        ElevatorModule.getInstance().setPivot(pivot);
    }

    /**
     * Sets the height of the elevator
     */
    public static void setElevatorHeight(double extension) {
        ElevatorModule.getInstance().setExtension(extension);
    }

    /**
     * Sets the robot to the ground/starting state
     */
    public static void setGroundState() {
        ElevatorModule.getInstance().setElevatorState(ElevatorState.kStow);
    }

    /**
     * Sets the robot to the amp position
     */
    public static void setAmpState() {
        ElevatorModule.getInstance().setElevatorState(ElevatorState.kAmp);
    }

    /**
     * Sets the robot to the climb position
     */
    public static void setClimbState() {
        ElevatorModule.getInstance().setElevatorState(ElevatorState.kClimb);
    }

    /**
     * Extends the elevator
     */
    public static void raiseClimber() {
        ElevatorModule.getInstance().moveElevator(0.005);
    }

    /**
     * Retracts the elevator
     */
    public static void lowerClimber() {
        ElevatorModule.getInstance().moveElevator(-0.005);
    }
}