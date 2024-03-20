package frc.robot.modes;

import frc.robot.common.Constants;
import frc.robot.controls.XboxController;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.FeederModule.FeederState;

public abstract class ModeManager {
    protected XboxController driverController, operatorController; // Physical controllers used by the drivers

    /**
     * Passes in the driver and operator {@code XboxControllers} to all mode managers
     */
    public void setControllers(XboxController driver, XboxController operator) {
        this.driverController = driver;
        this.operatorController = operator;
    }

    /**
     * Runs through the entirety of the current match mode
     */
    public abstract void runPeriodic();

    /**
     * Intakes a note
     */
    protected void setIntaking() {
        IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
        FeederModule.getInstance().setFeederState(FeederState.kIntake);
    }

    /**
     * Powers off intake and feeder
     */
    protected void stopAllRollers() {
        IntakeModule.getInstance().setRollerVelocity(0.0);
        FeederModule.getInstance().setFeederState(FeederState.kOff);
    }

    /**
     * Spit out a note
     */
    protected void setOutaking() {
        IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTTAKE_RPM);
        FeederModule.getInstance().setFeederState(FeederState.kOutake);
    }
}