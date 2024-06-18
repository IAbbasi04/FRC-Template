package frc.robot.subsystems;

import frc.robot.common.Ports;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.motors.VortexMotor;

public class IntakeSubsystem extends Subsystem {
    private static IntakeSubsystem INSTANCE = null;
    public static IntakeSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new IntakeSubsystem();
        return INSTANCE;
    }

    private VortexMotor rollerMotor;
    private double desiredRollerRPM = 0.0;

    private IntakeSubsystem() {
        rollerMotor = new VortexMotor(Ports.INTAKE_ROLLER_CAN_ID);
    }

    /**
     * Sets the desired speed of the roller motor in RPM
     */
    public void setRollerVelocity(double desiredRPM) {
        this.desiredRollerRPM = desiredRPM;
    }

    /**
     * The current speed of the roller motor in RPM
     */
    public double getRollerVelocity() {
        return rollerMotor.getVelocity();
    }

    @Override
    public void init(MatchMode mode) {
        setRollerVelocity(0.0);
    }

    @Override
    public void initializeLogs() {

    }

    @Override
    public void periodic() {
        
    }
}