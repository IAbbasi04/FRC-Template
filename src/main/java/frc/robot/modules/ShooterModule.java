package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Ports;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.motors.VortexMotor;
import frc.robot.hardware.motors.Motor.ControlType;

public class ShooterModule extends Module {
    private static ShooterModule INSTANCE = null;
    public static ShooterModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShooterModule();
        }
        return INSTANCE;
    }

    private VortexMotor leftShooterMotor, rightShooterMotor;

    private double desiredLeftRPM, desiredRightRPM;

    private ShooterModule() {
        leftShooterMotor = new VortexMotor(Ports.LEFT_SHOOTER_MOTOR_CAN_ID);
        rightShooterMotor = new VortexMotor(Ports.RIGHT_SHOOTER_MOTOR_CAN_ID, true);
        super.addLogger("Shooter", Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Current velocity of the left shooter motor
     */
    public double getLeftFlywheelRPM() {
        return leftShooterMotor.getVelocity();
    }

    /**
     * Current velocity of the right shooter motor
     */
    public double getRightFlywheelRPM() {
        return rightShooterMotor.getVelocity();
    }
    
    /**
     * Desired velocity of the left motor
     */
    public double getDesiredLeftRPM() {
        return desiredLeftRPM;
    }

    /**
     * Desired velocity of the right motor
     */
    public double getDesiredRightRPM() {
        return desiredRightRPM;
    }

    /**
     * Sets the desired velocity of both shooter motors
     */
    public void setDesiredRPM(double leftRPM, double rightRPM) {
        this.desiredLeftRPM = leftRPM;
        this.desiredRightRPM = rightRPM;
    }

    /**
     * If both shooter motors are within the velocity tolerance
     */
    public boolean atTargetVelocity() {
        boolean left = Math.abs(leftShooterMotor.getVelocity() - desiredLeftRPM) <= Constants.SHOOTER.TARGET_TOLERANCE;
        boolean right = Math.abs(rightShooterMotor.getVelocity() - desiredRightRPM) <= Constants.SHOOTER.TARGET_TOLERANCE;
        return left && right;
    }

    @Override
    public void init(MatchMode mode) {
        leftShooterMotor.set(ControlType.kVelocity, 0.0);
        rightShooterMotor.set(ControlType.kVelocity, 0.0);
    }

    @Override
    public void initializeLogs() {
        logger.setNumber("Desired Left RPM", () -> getDesiredLeftRPM());
        logger.setNumber("Desired Right RPM", () -> getDesiredRightRPM());
        logger.setNumber("Current Left RPM", () -> getLeftFlywheelRPM());
        logger.setNumber("Current Right RPM", () -> getRightFlywheelRPM());
        logger.setBoolean("At Desired RPM", () -> atTargetVelocity());
    }

    @Override
    public void periodic() {
        leftShooterMotor.set(ControlType.kVelocity, desiredLeftRPM);
        rightShooterMotor.set(ControlType.kVelocity, desiredRightRPM);
    }
}