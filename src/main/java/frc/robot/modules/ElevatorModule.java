package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.*;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.motors.VortexMotor;
import frc.robot.hardware.motors.Motor.ControlType;

import static frc.robot.common.Constants.*;

public class ElevatorModule extends Module {
    private static ElevatorModule INSTANCE = null;
    public static ElevatorModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ElevatorModule();
        }
        return INSTANCE;
    }

    public enum ElevatorState {
        kDefault(0, 0),
        kStow(0, 0),
        kAmp(0.27, 45),
        kClimb(0.279, 70);

        public double extension, pivot;
        private ElevatorState(double extension, double pivot) {
            this.extension = extension;
            this.pivot = pivot;
        }
    }

    private ProfileGains pivotUpGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(6500.0)
        .setMaxVelocity(5000.0)
    ;

    private ProfileGains pivotDownGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(6500.0)
        .setMaxVelocity(5000.0)
    ;

    private ProfileGains pivotClimbGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(6500.0)
        .setMaxVelocity(5000.0)
    ;

    private ProfileGains extensionUpGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(5000.0)
        .setMaxVelocity(10000.0)
    ;

    private ProfileGains extensionDownGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(5000.0)
        .setMaxVelocity(10000.0)
    ;

    private ProfileGains extensionClimbGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(5000.0)
        .setMaxVelocity(10000.0)
    ;

    private VortexMotor leftPivotMotor, rightPivotMotor, extensionMotor;
    private double desiredAngle, desiredExtension;
    private ElevatorState desiredState;

    private ElevatorModule() {
        leftPivotMotor = new VortexMotor(Ports.PIVOT_MOTOR_CAN_ID);
        rightPivotMotor = new VortexMotor(Ports.PIVOT_FOLLOW_MOTOR_CAN_ID, true);
        extensionMotor = new VortexMotor(Ports.ELEVATOR_MOTOR_CAN_ID);

        leftPivotMotor.withGains(pivotUpGains, 0);
        leftPivotMotor.withGains(pivotDownGains, 1);
        leftPivotMotor.withGains(pivotClimbGains, 2);

        rightPivotMotor.withGains(pivotUpGains, 0);
        rightPivotMotor.withGains(pivotDownGains, 1);
        rightPivotMotor.withGains(pivotClimbGains, 2);

        extensionMotor.withGains(extensionUpGains, 0);
        extensionMotor.withGains(extensionDownGains, 1);
        extensionMotor.withGains(extensionClimbGains, 2);

        desiredAngle = 0.0;
        desiredExtension = 0.0;

        desiredState = ElevatorState.kDefault;

        super.addLogger("Elevator", Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Sets the distance in meters for the elevator to extend
     */
    public void setExtension(double meters) {
        this.desiredExtension = meters;
        desiredState = ElevatorState.kDefault;
    }

    /**
     * Sets the distance in meters for the elevator to extend
     */
    public void setPivot(double degrees) {
        this.desiredAngle = degrees;
        desiredState = ElevatorState.kDefault;
    }

    /**
     * Raises or lowers desired extension position of the elevator
     */
    public void moveElevator(double deltaMeters) {
        setExtension(desiredExtension + deltaMeters);
    }

    /**
     * Raises or lowers desired angle of the pivot
     */
    public void movePivot(double deltaDegrees) {
        setPivot(desiredAngle + deltaDegrees);
    }

    /**
     * Current angle of the pivot in degrees
     */
    public double getPivotAngleDegrees() {
        return Conversions.rotationsToDegrees(leftPivotMotor.getPosition()) * ELEVATOR.PIVOT_RATIO;
    }

    /**
     * Current extension of the elevator in meters
     */
    public double getElevatorExtensionMeters() {
        return extensionMotor.getPosition() * ELEVATOR.EXTENSION_RATIO;
    }

    /**
     * Desired angle in degrees for the pivot
     */
    public double getDesiredPivotAngle() {
        return desiredAngle;
    }

    /**
     * Desired exntension in meters for the elevator
     */
    public double getDesiredExtension() {
        return desiredExtension;
    }

    /**
     * Sets the desired state of the elevator
     */
    public void setElevatorState(ElevatorState state) {
        this.desiredState = state;
    }

    /**
     * The desired state of the elevator
     */
    public ElevatorState getElevatorState() {
        return desiredState;
    }

    /**
     * Both extension and pivot are at desired positions
     */
    public boolean atTargetPosition() {
        boolean pivot = Math.abs(getPivotAngleDegrees() - desiredAngle) <= ELEVATOR.PIVOT_TOLERANCE;
        boolean extension = Math.abs(getElevatorExtensionMeters() - desiredExtension) <= ELEVATOR.EXTENSION_TOLERANCE;
        return pivot && extension;
    }

    @Override
    public void init(MatchMode mode) {
        extensionMotor.set(ControlType.kVelocity, 0.0);
        leftPivotMotor.set(ControlType.kVelocity, 0.0);
        rightPivotMotor.set(ControlType.kVelocity, 0.0);
    }

    @Override
    public void initializeLogs() {
        logger.setNumber("Current Angle Degrees", () -> getPivotAngleDegrees());
        logger.setNumber("Current Extension Meters", () -> getElevatorExtensionMeters());
        logger.setNumber("Desired Pivot", () -> getDesiredPivotAngle());
        logger.setNumber("Desired Extension", () -> getDesiredExtension());
        logger.setBoolean("At Target", () -> atTargetPosition());
        logger.setEnum("Elevator State", () -> getElevatorState());
    }

    @Override
    public void periodic() {
        double outputExtension = desiredExtension;
        double outputAngle = desiredAngle;

        switch (desiredState) {
            case kStow:
            case kAmp:
            case kClimb: // All above states use the enum constant's elevator values
                desiredExtension = desiredState.extension;
                desiredAngle = desiredState.pivot;
                break;
            default:
                // Do nothing extra
                break;
        }

        // Clamp desired extension within range
        if (desiredExtension >= ELEVATOR.MAX_EXTENSION) {
            desiredExtension = ELEVATOR.MAX_EXTENSION;
        } else if (desiredExtension <= ELEVATOR.MIN_EXTENSION) {
            desiredExtension = ELEVATOR.MIN_EXTENSION;
        }

        // Clamp desired angle within range
        if (desiredAngle >= ELEVATOR.MAX_PIVOT) {
            desiredAngle = ELEVATOR.MAX_PIVOT;
        } else if (desiredAngle <= ELEVATOR.MIN_PIVOT) {
            desiredAngle = ELEVATOR.MIN_PIVOT;
        }

        // Extension
        if (desiredExtension >= getElevatorExtensionMeters()) { // Extending
            if (getPivotAngleDegrees() >= ELEVATOR.EXTENSION_PIVOT_THRESHOLD) { // Pivot above threshold
                outputExtension = desiredExtension;
            } else { // Pivot too low
                outputExtension = getElevatorExtensionMeters();
            }
        } else if (desiredExtension < getElevatorExtensionMeters()) { // Retracting
            if (getPivotAngleDegrees() <= ELEVATOR.EXTENSION_PIVOT_THRESHOLD) { // Pivot above threshold
                outputExtension = getElevatorExtensionMeters();
                outputAngle = ELEVATOR.EXTENSION_PIVOT_THRESHOLD + 5.0; // Adding a few degrees because of error margin
            } else { // Pivot too low
                outputExtension = desiredExtension;
            }
        }

        // Pivot
        if (desiredAngle >= getPivotAngleDegrees()) { // Raising
            outputAngle = desiredAngle;
        } else if (desiredAngle < getPivotAngleDegrees()) { // Lowering
            if (getElevatorExtensionMeters() >= ELEVATOR.PIVOT_EXTENSION_THRESHOLD) { // Elevator currently extended
                outputAngle = getPivotAngleDegrees();
            } else { // Extension below threshold
                outputAngle = desiredAngle;
            }
        }

        double outputPivotRotations = Conversions.degreesToRotations(outputAngle / ELEVATOR.PIVOT_RATIO);
        double outputExtensionRotations = outputExtension / ELEVATOR.EXTENSION_RATIO;

        leftPivotMotor.set(ControlType.kPosition, outputPivotRotations);
        rightPivotMotor.set(ControlType.kPosition, outputPivotRotations);
        extensionMotor.set(ControlType.kPosition, outputExtensionRotations);
    }
}