package frc.robot.subsystems;

import frc.robot.common.Ports;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.motors.KrakenMotor;
import lib.frc8592.hardware.motors.Motor;
import lib.frc8592.logging.SmartLogger;

public class BackpackSubsystem extends Subsystem {
    private static BackpackSubsystem INSTANCE = null;
    public static BackpackSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new BackpackSubsystem();
        return INSTANCE;
    }

    public enum BackpackState {
        kOverride,
        kStow,
        kLoad,
        kStage,
        kRetrieve,
        kScore,
    }

    private Motor frontLiftMotor, backLiftMotor;
    private Motor rollerMotor;

    private double desiredRollerVelocityRPM = 0.0;
    private double desiredBackpackHeightInches = 0.0;

    private BackpackState backpackState;

    private BackpackSubsystem() {
        frontLiftMotor = new KrakenMotor(Ports.BACKPACK_FRONT_LIFT_CAN_ID);
        backLiftMotor = new KrakenMotor(Ports.BACKPACK_BACK_LIFT_CAN_ID);
        rollerMotor = new KrakenMotor(Ports.BACKPACK_ROLLERS_CAN_ID);

        super.logger = new SmartLogger("BackpackSubsystem");
    }

    /**
     * Sets the desired speed of the roller motor in RPM
     */
    public void setRollerVelocity(double desiredVelocityRPM) {
        this.desiredRollerVelocityRPM = desiredVelocityRPM;
    }

    /**
     * Sets the desired height of the backpack system in inches
     */
    public void setBackpackHeight(double desiredHeightInches) {
        this.desiredBackpackHeightInches = desiredHeightInches;
    }

    /**
     * The current speed of the roller motor in RPM
     */
    public double getRollerVelocity() {
        return this.rollerMotor.getVelocity();
    }

    /**
     * The current speed of the roller motor in RPM
     */
    public double getBackpackHeight() {
        return (this.frontLiftMotor.getPosition() + this.backLiftMotor.getPosition()) / 2.0; // TODO - Fix this
    }

    @Override
    public void init(MatchMode mode) {
        setRollerVelocity(0.0);
    }

    @Override
    public void initializeLogs() {
        logger.logDouble("Desired Roller Velocity (RPM)", () -> desiredRollerVelocityRPM);
        logger.logDouble("Desired Backpack Height (in)", () -> desiredBackpackHeightInches);
        logger.logDouble("Current Backpack Height (in)", () -> getBackpackHeight());
        logger.logDouble("Current Roller Velocity (RPM)", () -> getRollerVelocity());
    }

    @Override
    public void periodic() {
        switch (backpackState) {
            case kStow:

                break;
            case kLoad:

                break;
            case kOverride:

                break;
            case kRetrieve:

                break;
            case kScore:

                break;
            case kStage:

                break;
            default:

                break;
        }

        rollerMotor.setVelocity(desiredRollerVelocityRPM);
        frontLiftMotor.setPosition(desiredBackpackHeightInches);
        backLiftMotor.setPosition(desiredBackpackHeightInches);
    }
}