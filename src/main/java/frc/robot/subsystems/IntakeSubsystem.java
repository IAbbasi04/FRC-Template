package frc.robot.subsystems;

import frc.robot.common.Ports;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.BeamSensor;
import lib.frc8592.hardware.motors.*;
import lib.frc8592.logging.SmartLogger;

public class IntakeSubsystem extends Subsystem {
    private static IntakeSubsystem INSTANCE = null;
    public static IntakeSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new IntakeSubsystem();
        return INSTANCE;
    }

    private Motor armMotor;
    private Motor rollerMotor;
    private Motor feederMotor;

    private BeamSensor entryBeam;

    private double desiredRollerRPM = 0.0;
    private double desiredFeederRPM = 0.0;
    private double desiredArmDegrees = 0.0;

    private IntakeSubsystem() {
        armMotor = new KrakenMotor(Ports.INTAKE_ARM_CAN_ID);
        rollerMotor = new KrakenMotor(Ports.INTAKE_ROLLER_CAN_ID);
        feederMotor = new KrakenMotor(Ports.INTAKE_FEED_CAN_ID);
        entryBeam = new BeamSensor(Ports.ENTRY_BEAM_CAN_ID);

        super.logger = new SmartLogger("IntakeSubsystem");
    }

    /**
     * Sets the position of the arm motor in degrees
     */
    public void setArmAngle(double positionDegrees) {
        this.desiredArmDegrees = positionDegrees;
    }

    /**
     * Sets the speed of the roller motor in RPM
     */
    public void setRollerVelocity(double velocityRPM) {
        this.desiredRollerRPM = velocityRPM;
    }

    /**
     * Sets the speed of the feeder motor in RPM
     */
    public void setFeederVelocity(double velocityRPM) {
        this.desiredFeederRPM = velocityRPM;
    }

    /**
     * The current speed of the roller motor in RPM
     */
    public double getRollerVelocity() {
        return rollerMotor.getVelocity();
    }

    /**
     * The current speed of the feeder motor in RPM
     */
    public double getFeederVelocity() {
        return feederMotor.getVelocity();
    }

    /**
     * The current position of the arm motor in degrees
     */
    public double getArmPosition() {
        return armMotor.getPosition();
    }

    /**
     * Whether the beam of the beam break is broken
     */
    public boolean isBeamTripped() {
        return entryBeam.isBroken();
    }

    @Override
    public void init(MatchMode mode) {
        this.desiredFeederRPM = 0.0;
        this.desiredRollerRPM = 0.0;
    }

    @Override
    public void initializeLogs() {
        logger.logDouble("Desired Roller RPM", () -> desiredRollerRPM);
        logger.logDouble("Desired Feeder RPM", () -> desiredFeederRPM);
        logger.logDouble("Desired Arm Degrees", () -> desiredArmDegrees);

        logger.logDouble("Current Roller RPM", () -> getRollerVelocity());
        logger.logDouble("Current Feeder RPM", () -> getFeederVelocity());
        logger.logDouble("Current Arm Degrees", () -> getArmPosition());
    }

    @Override
    public void periodic() {
        rollerMotor.setVelocity(desiredRollerRPM);
        feederMotor.setVelocity(desiredFeederRPM);
        armMotor.setPosition(desiredArmDegrees);
    }
}