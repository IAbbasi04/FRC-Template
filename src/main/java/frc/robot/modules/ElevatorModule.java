package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.*;
import frc.robot.common.Enums.MatchMode;

import static frc.robot.common.Constants.*;

import com.lib.team1885.ProfileGains;
import com.lib.team8592.Conversions;
import com.lib.team8592.hardware.motors.VortexMotor;
import com.lib.team8592.hardware.motors.Motor.ControlType;

public class ElevatorModule extends Module {
    private static ElevatorModule INSTANCE = null;
    public static ElevatorModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ElevatorModule();
        }
        return INSTANCE;
    }

    public enum ElevatorState {
        kDefault(0, 0, 0, 0),
        kStow(0, 0, 1, 1),
        kAmp(0.27, 45, 0, 0),
        kClimb(0.279, 70, 2, 2);

        public double extension, pivot;
        public int pivotSlot, extensionSlot;
        private ElevatorState(double extension, double pivot, int pivotSlot, int extensionSlot) {
            this.extension = extension;
            this.pivot = pivot;
            this.pivotSlot = pivotSlot;
            this.extensionSlot = extensionSlot;
        }
    }

    private ProfileGains pivotDefaultGains = new ProfileGains()
        .setP(1E-6)
        .setFF(2.5E-4)
        .setMaxAccel(6500.0)
        .setMaxVelocity(5000.0)
    ;

    private ProfileGains pivotStowGains = new ProfileGains()
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

        leftPivotMotor.withGains(pivotDefaultGains, 0);
        leftPivotMotor.withGains(pivotStowGains, 1);
        leftPivotMotor.withGains(pivotClimbGains, 2);

        rightPivotMotor.withGains(pivotDefaultGains, 0);
        rightPivotMotor.withGains(pivotStowGains, 1);
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

        int pivotSlot = 0;
        int extensionSlot = 0;

        switch (desiredState) {
            case kStow:
            case kAmp:
            case kClimb: // All above states use the enum constant's values
                desiredExtension = desiredState.extension;
                desiredAngle = desiredState.pivot;

                pivotSlot = desiredState.pivotSlot;
                extensionSlot = desiredState.extensionSlot;
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

            if (desiredState != ElevatorState.kClimb) {
                extensionSlot = extensionUpGains.getSlot();
            }
        } else if (desiredExtension < getElevatorExtensionMeters()) { // Retracting
            if (getPivotAngleDegrees() <= ELEVATOR.EXTENSION_PIVOT_THRESHOLD) { // Pivot above threshold
                outputExtension = getElevatorExtensionMeters();
                outputAngle = ELEVATOR.EXTENSION_PIVOT_THRESHOLD + 5.0; // Adding a few degrees because of error margin
            } else { // Pivot too low
                outputExtension = desiredExtension;
            }

            if (desiredState != ElevatorState.kClimb) {
                extensionSlot = extensionDownGains.getSlot();
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

        leftPivotMotor.set(ControlType.kPosition, outputPivotRotations, pivotSlot);
        rightPivotMotor.set(ControlType.kPosition, outputPivotRotations, pivotSlot);
        extensionMotor.set(ControlType.kPosition, outputExtensionRotations, extensionSlot);
    }
}