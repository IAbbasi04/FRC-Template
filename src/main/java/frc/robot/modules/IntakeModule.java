package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.Ports;
import frc.robot.common.ProfileGains;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.motors.VortexMotor;
import frc.robot.hardware.motors.Motor.ControlType;

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

    }

    @Override
    public void periodic() {
        rollerMotor.set(ControlType.kVelocity, desiredRollerRPM);

        SmartDashboard.putNumber("INTAKE/CURRENT VELOCITY", this.getRollerVelocity());
        SmartDashboard.putNumber("INTAKE/DESIRED VELOCITY", this.desiredRollerRPM);
    }
}