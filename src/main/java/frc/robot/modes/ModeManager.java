package frc.robot.modes;

import lib.frc8592.controls.xbox.XboxController;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;
import frc.robot.subsystems.FeederSubsystem.FeederState;
import frc.robot.subsystems.LEDSubsystem.LEDMode;

public abstract class ModeManager {
    // Physical controllers used by the drivers
    protected XboxController driverController = new XboxController(Constants.INPUT.DRIVER_CONTROLLER_PORT);
    protected XboxController operatorController = new XboxController(Constants.INPUT.DRIVER_CONTROLLER_PORT);

    /**
     * Updates inputs for the leds the entire match mode
     */
    protected void updateLED() {
        LEDMode desiredLEDMode = LEDMode.kOff;
        switch (FeederSubsystem.getInstance().getNoteState()) {
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

        LEDSubsystem.getInstance().setLEDMode(desiredLEDMode);
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();

    /**
     * Allows the robot to intake a note and stage it accordingly in the feeder
     */
    public static void setIntaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
        FeederSubsystem.getInstance().setFeederState(FeederState.kIntake);
    }

    /**
     * Allows the robot to spit out a note for any reason in any state
     */
    public static void setOutaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTAKE_RPM);
        FeederSubsystem.getInstance().setFeederState(FeederState.kOutake);
    }

    /**
     * Stop spinning the intake roller
     */
    public static void stopIntake() {
        IntakeSubsystem.getInstance().setRollerVelocity(0.0);
    }

    /**
     * Set the feeder state to the kOff state
     */
    public static void stopFeeder() {
        FeederSubsystem.getInstance().setFeederState(FeederState.kOff);
    }

    /**
     * Resets the robot to the starting ground state of the robot.
     * 
     * <p> - Elevator System in {@code ElevatorState.kStowed} </p> 
     * <p> - Feeder Rollers in {@code FeederState.kOff} </p> 
     * <p> - Intake Rollers turned off </p>
     */
    public static void setGroundState() {
        ModeManager.stopIntake();
        ModeManager.stopFeeder();
        ModeManager.stopShooter();
        ModeManager.setStowState();
    }

    /**
     * Sets all parameters for shooting
     */
    public static void setShooting(double leftRPM, double rightRPM, double pivotAngle, double elevatorHeight, boolean shouldShoot) {
        ShooterSubsystem.getInstance().setDesiredRPM(leftRPM, rightRPM);
        ElevatorSubsystem.getInstance().setPivot(pivotAngle);
        ElevatorSubsystem.getInstance().setExtension(elevatorHeight);
        if ((ShooterSubsystem.getInstance().atTargetVelocity() && 
            ElevatorSubsystem.getInstance().atTargetPosition() &&
            shouldShoot) || Robot.isSimulation()) {
            FeederSubsystem.getInstance().setFeederState(FeederState.kShoot);
        }
    }

    /**
     * Sets the robot to a shooting state given the desired shot profile
     */
    public static void setShooting(ShotProfile desiredShotProfile) {
        ModeManager.setShooting(
            desiredShotProfile.leftShotRPM, 
            desiredShotProfile.rightShotRPM, 
            desiredShotProfile.pivotDegrees, 
            desiredShotProfile.extensionMeters, 
            desiredShotProfile.shouldShoot
        );
    }

    /**
     * Sets the velocity of the shooter motors
     */
    public static void setShooterVelocity(double leftRPM, double rightRPM) {
        ShooterSubsystem.getInstance().setDesiredRPM(leftRPM, rightRPM);
    }

    /**
     * Sets the override feeder velocity
     */
    public static void setFeederVelocity(double feederRPM) {
        FeederSubsystem.getInstance().setFeederVelocity(feederRPM);
    }

    /**
     * Turns off shooter motors
     */
    public static void stopShooter() {
        ModeManager.setShooterVelocity(0, 0);
    }

    /**
     * Sets the angle of the pivot
     */
    public static void setPivotAngle(double pivot) {
        ElevatorSubsystem.getInstance().setPivot(pivot);
    }

    /**
     * Sets the height of the elevator
     */
    public static void setElevatorHeight(double extension) {
        ElevatorSubsystem.getInstance().setExtension(extension);
    }

    /**
     * Shooter and feeder presets for scoring in amp
     */
    public static void scoreAmp() {
        setShooterVelocity(-1000, -1000);
        FeederSubsystem.getInstance().setFeederVelocity(-3000);
    }

    /**
     * Sets the robot to the amp position
     */
    public static void setStowState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kStow);
    }

    /**
     * Sets the robot to the amp position
     */
    public static void setAmpState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kAmp);
    }

    /**
     * Sets the robot to the climb position
     */
    public static void setClimbState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kClimb);
    }

    /**
     * Extends the elevator
     */
    public static void raiseClimber() {
        ElevatorSubsystem.getInstance().moveElevator(0.005);
    }

    /**
     * Retracts the elevator
     */
    public static void lowerClimber() {
        ElevatorSubsystem.getInstance().moveElevator(-0.005);
    }
}