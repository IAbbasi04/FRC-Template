package frc.robot.modules;

import com.lib.team8592.logging.SmartLogger;

import frc.robot.Robot;
import frc.robot.common.Enums.MatchMode;

import com.lib.team8592.hardware.motors.SimMotor;
import com.lib.team8592.hardware.motors.Motor.ControlType;

public class ExampleModule extends Module {
    private static ExampleModule INSTANCE = null;
    public static ExampleModule getInstance() {
        if (INSTANCE == null) INSTANCE = new ExampleModule();
        return INSTANCE;
    }

    private double desiredExampleVelocity = 0.0;

    private SimMotor exampleMotor;

    private ExampleModule() {
        exampleMotor = new SimMotor(0);

        super.logger = new SmartLogger("Example", Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Sets the desired example velocity in RPM
     */
    public void setDesiredVelocity(double velocity) {
        this.desiredExampleVelocity = velocity;
    }

    public double getDesiredVelocity() {
        return this.desiredExampleVelocity;
    }

    /**
     * The current example velocity
     */
    public double getExampleVelocity() {
        return exampleMotor.getVelocity();
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {
        logger.setNumber("Desired Example Velocity", () -> this.getDesiredVelocity());
        logger.setNumber("Current Example Velocity", () -> this.getExampleVelocity());
    }

    @Override
    public void periodic() {
        exampleMotor.set(ControlType.kVelocity, desiredExampleVelocity);
    }
}