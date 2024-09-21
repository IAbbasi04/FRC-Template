package frc.robot.subsystems;

import lib.frc8592.MatchMode;
import lib.frc8592.hardware.motors.FalconMotor;
import lib.frc8592.hardware.motors.Motor.ControlType;
import lib.frc8592.logging.SmartLogger;

public class TestSubsystem extends Subsystem {
    private static TestSubsystem INSTANCE = null;
    public static TestSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new TestSubsystem();
        return new TestSubsystem();
    }

    private FalconMotor testBedFalcon;
    private double desiredVelocityRPM = 0.0;

    private TestSubsystem() {
        testBedFalcon = new FalconMotor(30);
        super.logger = new SmartLogger("TestSubsystem");
    }

    /**
     * Sets desired velocity in RPM
     */
    public void setDesiredRPM(double velocityRPM) {
        this.desiredVelocityRPM = velocityRPM;
    }

    @Override
    public void init(MatchMode mode) {
        testBedFalcon.set(ControlType.kVelocity, 0.0);
    }

    @Override
    public void initializeLogs() {
        logger.logDouble("Desired Velocity RPM", () -> this.desiredVelocityRPM);
        logger.logDouble("Current Velocity RPM", () -> this.testBedFalcon.getVelocity());
    }

    @Override
    public void periodic() {
        testBedFalcon.set(ControlType.kVelocity, this.desiredVelocityRPM);
    }
}