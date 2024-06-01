package frc.robot.subsystems;

import frc.robot.common.Ports;
import lib.frc8592.*;
import lib.frc8592.hardware.motors.*;
import lib.frc8592.hardware.motors.Motor.ControlType;
import lib.frc8592.logging.SmartLogger;

public class IntakeSubsystem extends Subsystem {
    private static IntakeSubsystem INSTANCE = null;
    public static IntakeSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new IntakeSubsystem();
        return INSTANCE;
    }

    private Motor intakeMotor;
    private double desiredVelocityRPM;
    private double maxVelocity = 6000.0;

    private IntakeSubsystem() {
        intakeMotor = new VortexMotor(Ports.INTAKE_ROLLER_CAN_ID);
        intakeMotor.withGains(
            new ProfileGains()
                .setP(1E-4)
                .setFF(5E-3)
                .setI(1E-6), 
            0
        );

        super.logger = new SmartLogger("IntakeSubsystem");
    }

    /**
     * Desired Roller Speed in RPM
     */
    public double getDesiredVelocity() {
        return this.desiredVelocityRPM;
    }

    /**
     * Sets desired roller speed in RPM
     */
    public void setRollerVelocity(double desiredVelocityRPM) {
        // Clamp within maximum allowed RPM
        this.desiredVelocityRPM = Utils.clamp(desiredVelocityRPM, maxVelocity);
    }

    /**
     * Current roller speed in RPM
     */
    public double getCurrentVelocity() {
        return this.intakeMotor.getVelocity();
    }

    @Override
    public void init(MatchMode mode) {
        // Start each match mode with the intake turned off
        this.setRollerVelocity(0.0);
    }

    @Override
    public void initializeLogs() {
        this.logger.logDouble("Desired Velocity RPM", () -> getDesiredVelocity());
        this.logger.logDouble("Current Velocity RPM", () -> getCurrentVelocity());
    }

    @Override
    public void periodic() {
        this.intakeMotor.set(ControlType.kVelocity, desiredVelocityRPM);        
    }
}