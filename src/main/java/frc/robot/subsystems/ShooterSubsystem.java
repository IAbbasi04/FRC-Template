package frc.robot.subsystems;

import frc.robot.common.Constants;
import frc.robot.common.Ports;
import lib.frc8592.MatchMode;
import lib.frc8592.ProfileGains;
import lib.frc8592.hardware.motors.Motor;
import lib.frc8592.hardware.motors.VortexMotor;
import lib.frc8592.hardware.motors.Motor.ControlType;
import lib.frc8592.logging.SmartLogger;

public class ShooterSubsystem extends Subsystem {
    private static ShooterSubsystem INSTANCE = null;
    public static ShooterSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new ShooterSubsystem();
        return INSTANCE;
    }

    private Motor leftMotor, rightMotor;
    private double desiredLeftRPM, desiredRightRPM;

    private ProfileGains shooterVelocityGains = new ProfileGains()
        .setP(0.0001)
        .setFF(0.001)
        ;

    private ShooterSubsystem() {
        this.leftMotor = new VortexMotor(Ports.LEFT_SHOOTER_MOTOR_CAN_ID, false);
        this.rightMotor = new VortexMotor(Ports.RIGHT_SHOOTER_MOTOR_CAN_ID, true);

        this.leftMotor.withGains(shooterVelocityGains, 0);
        this.rightMotor.withGains(shooterVelocityGains, 0);

        super.logger = new SmartLogger("ShooterSubsystem");
    }

    /**
     * Sets the speed for both shooter motors in RPM
     */
    public void setDesiredRPM(double desiredLeftVelocityRPM, double desiredRightVelocityRPM) {
        this.desiredLeftRPM = desiredLeftVelocityRPM;
        this.desiredRightRPM = desiredRightVelocityRPM;
    }

    /**
     * The desired speed of the left shooter motor in RPM
     */
    public double getDesiredLeftVelocity() {
        return this.desiredLeftRPM;
    }

    /**
     * The desired speed of the right shooter motor in RPM
     */
    public double getDesiredRightVelocity() {
        return this.desiredRightRPM;
    }

    /**
     * The current velocity of the left shooter motor in RPM
     */
    public double getCurrentLeftVelocity() {
        return this.leftMotor.getVelocity();
    }

    /**
     * The current velocity of the right shooter motor in RPM
     */
    public double getCurrentRightVelocity() {
        return this.rightMotor.getVelocity();
    }

    /**
     * If both shooter motors are within the velocity tolerance
     */
    public boolean atTargetVelocity() {
        boolean left = Math.abs(leftMotor.getVelocity() - desiredLeftRPM) <= Constants.SHOOTER.TARGET_TOLERANCE;
        boolean right = Math.abs(rightMotor.getVelocity() - desiredRightRPM) <= Constants.SHOOTER.TARGET_TOLERANCE;
        return left && right;
    }

    @Override
    public void init(MatchMode mode) {
        leftMotor.set(ControlType.kVelocity, 0.0);
        rightMotor.set(ControlType.kVelocity, 0.0);
    }

    @Override
    public void initializeLogs() {
        this.logger.logDouble("Desired Left Velocity (RPM)", () -> getDesiredLeftVelocity());
        this.logger.logDouble("Desired Right Velocity (RPM)", () -> getDesiredRightVelocity());
        this.logger.logDouble("Current Left Velocity (RPM)", () -> getCurrentLeftVelocity());
        this.logger.logDouble("Current Right Velocity (RPM)", () -> getCurrentRightVelocity());
        logger.logBoolean("At Desired RPM", () -> atTargetVelocity());
    }

    @Override
    public void periodic() {
        this.leftMotor.set(ControlType.kVelocity, desiredLeftRPM);
        this.rightMotor.set(ControlType.kVelocity, desiredRightRPM);
    }
}