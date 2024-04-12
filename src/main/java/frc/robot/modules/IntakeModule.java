package frc.robot.modules;

import com.lib.team1885.ProfileGains;
import com.lib.team8592.hardware.motors.VortexMotor;
import com.lib.team8592.hardware.motors.Motor.ControlType;

import frc.robot.Robot;
import frc.robot.common.Ports;
import frc.robot.common.Enums.MatchMode;

public class IntakeModule extends Module {
    private static IntakeModule INSTANCE = null;
    public static IntakeModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IntakeModule();
        }
        return INSTANCE;
    }

    private VortexMotor rollerMotor;
    private double desiredRollerRPM;

    private ProfileGains rollerVelocityGains = new ProfileGains()
        .setP(0.0003)
        .setD(0.015)
        .setFF(0.00016)
        .setCurrentLimit(60)
    ;

    private IntakeModule() {
        rollerMotor = new VortexMotor(Ports.INTAKE_ROLLER_CAN_ID);
        rollerMotor.withGains(rollerVelocityGains, 0);
        super.addLogger("Intake", Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Sets desired RPM demand for the roller motor
     */
    public void setRollerVelocity(double rpm) {
        this.desiredRollerRPM = rpm;
    }

    /**
     * RPM the motor (not roller) is currently spinning at
     */
    public double getRollerVelocity() {
        return rollerMotor.getVelocity();
    }

    @Override
    public void init(MatchMode mode) {
        rollerMotor.set(ControlType.kVelocity, 0);
    }

    @Override
    public void initializeLogs() {
        logger.setNumber("Current RPM", () -> getRollerVelocity());
        logger.setNumber("Desired RPM", () -> desiredRollerRPM);
    }

    @Override
    public void periodic() {
        rollerMotor.set(ControlType.kVelocity, desiredRollerRPM);
    }
}