package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;
import frc.robot.subsystems.FeederSubsystem.FeederState;

public class Superstructure {
    private static Superstructure INSTANCE;
    public static Superstructure getInstance() {
        if (INSTANCE == null) INSTANCE = new Superstructure();
        return INSTANCE;
    }

    /**
     * Allows the robot to intake a note and stage it accordingly in the feeder
     */
    public void setIntaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
        FeederSubsystem.getInstance().setFeederState(FeederState.kIntake);
    }

    /**
     * Allows the robot to spit out a note for any reason in any state
     */
    public void setOutaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTAKE_RPM);
        FeederSubsystem.getInstance().setFeederState(FeederState.kOutake);
    }

    /**
     * Stop spinning the intake roller
     */
    public void stopIntake() {
        IntakeSubsystem.getInstance().setRollerVelocity(0.0);
    }

    /**
     * Set the feeder state to the kOff state
     */
    public void stopFeeder() {
        FeederSubsystem.getInstance().setFeederState(FeederState.kOff);
    }

    /**
     * Resets the robot to the starting ground state of the robot.
     * 
     * <p> - Elevator System in {@code ElevatorState.kStowed} </p> 
     * <p> - Feeder Rollers in {@code FeederState.kOff} </p> 
     * <p> - Intake Rollers turned off </p>
     */
    public void setGroundState() {
        this.stopIntake();
        this.stopFeeder();
        this.stopShooter();
        this.setStowState();
    }

    /**
     * Sets all parameters for shooting
     */
    public void setShooting(double leftRPM, double rightRPM, double pivotAngle, double elevatorHeight, boolean shouldShoot) {
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
    public void setShooting(ShotProfile desiredShotProfile) {
        this.setShooting(
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
    public void setShooterVelocity(double leftRPM, double rightRPM) {
        ShooterSubsystem.getInstance().setDesiredRPM(leftRPM, rightRPM);
    }

    /**
     * Sets the override feeder velocity
     */
    public void setFeederVelocity(double feederRPM) {
        FeederSubsystem.getInstance().setFeederVelocity(feederRPM);
    }

    /**
     * Turns off shooter motors
     */
    public void stopShooter() {
        this.setShooterVelocity(0, 0);
    }

    /**
     * Sets the angle of the pivot
     */
    public void setPivotAngle(double pivot) {
        ElevatorSubsystem.getInstance().setPivot(pivot);
    }

    /**
     * Sets the height of the elevator
     */
    public void setElevatorHeight(double extension) {
        ElevatorSubsystem.getInstance().setExtension(extension);
    }

    /**
     * Shooter and feeder presets for scoring in amp
     */
    public void scoreAmp() {
        setShooterVelocity(-1000, -1000);
        FeederSubsystem.getInstance().setFeederVelocity(-3000);
    }

    /**
     * Sets the robot to the amp position
     */
    public void setStowState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kStow);
    }

    /**
     * Sets the robot to the amp position
     */
    public void setAmpState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kAmp);
    }

    /**
     * Sets the robot to the climb position
     */
    public void setClimbState() {
        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kClimb);
    }

    /**
     * Extends the elevator
     */
    public void raiseClimber() {
        ElevatorSubsystem.getInstance().moveElevator(0.005);
    }

    /**
     * Retracts the elevator
     */
    public void lowerClimber() {
        ElevatorSubsystem.getInstance().moveElevator(-0.005);
    }
}