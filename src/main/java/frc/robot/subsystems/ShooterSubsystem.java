package frc.robot.subsystems;

import frc.robot.common.Ports;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.motors.Motor;
import lib.frc8592.hardware.motors.VortexMotor;
import lib.frc8592.logging.SmartLogger;

public class ShooterSubsystem extends Subsystem {
    private static ShooterSubsystem INSTANCE = null;
    public static ShooterSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new ShooterSubsystem();
        return INSTANCE;
    }

    private Motor leftMotor, rightMotor;

    private double desiredLeftVelocityRPM = 0.0;
    private double desiredRightVelocityRPM = 0.0;

    public ShooterSubsystem() {
        leftMotor = new VortexMotor(Ports.SHOOTER_LEFT_CAN_ID);
        rightMotor = new VortexMotor(Ports.SHOOTER_RIGHT_CAN_ID);

        super.logger = new SmartLogger("ShooterSubsystem");
    }

    /**
     * Sets the desired speed of the left shooter motor in RPM
     */
    public void setLeftShooterVelocity(double desiredRPM) {
        desiredLeftVelocityRPM = desiredRPM;
    }

    /**
     * Sets the desired speed of the right shooter motor in RPM
     */
    public void setRightShooterVelocity(double desiredRPM) {
        desiredRightVelocityRPM = desiredRPM;
    }

    /**
     * Sets the desired speed of both shooter motors in RPM
     */
    public void setDesiredVelocity(double desiredLeftRPM, double desiredRightRPM) {
        setLeftShooterVelocity(desiredLeftRPM);
        setRightShooterVelocity(desiredRightRPM);
    }

    /**
     * Current speed of the left shooter motor in RPM
     */
    public double getLeftVelocity() {
        return leftMotor.getVelocity();
    }

    /**
     * Current speed of the left shooter motor in RPM
     */
    public double getRightVelocity() {
        return rightMotor.getVelocity();
    }

    /**
     * Whether the shooter motors are at the desired speed
     */
    public boolean isAtTargetSpeed() {
        boolean left = Math.abs(leftMotor.getVelocity() - desiredLeftVelocityRPM) <= 50;
        boolean right = Math.abs(rightMotor.getVelocity() - desiredRightVelocityRPM) <= 50;
        return left && right;
    }

    @Override
    public void init(MatchMode mode) {
        setDesiredVelocity(0.0, 0.0);
    }

    @Override
    public void initializeLogs() {
        logger.logDouble("Desired Left Velocity (RPM)", () -> desiredLeftVelocityRPM);
        logger.logDouble("Desired Right Velocity (RPM)", () -> desiredRightVelocityRPM);
        logger.logDouble("Current Left Velocity (RPM)", () -> getLeftVelocity());
        logger.logDouble("Current Right Velocity (RPM)", () -> getRightVelocity());
        logger.logBoolean("Is At Target Velocity?", () -> isAtTargetSpeed());
    }

    @Override
    public void periodic() {
        leftMotor.setVelocity(desiredLeftVelocityRPM);
        rightMotor.setVelocity(desiredRightVelocityRPM);
    }
}