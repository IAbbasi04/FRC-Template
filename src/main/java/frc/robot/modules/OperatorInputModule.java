package frc.robot.modules;

import frc.robot.common.Enums.MatchMode;
import org.frc8592.controls.xbox.XController;

public class OperatorInputModule extends Module {
    private static OperatorInputModule INSTANCE = null;
    public static OperatorInputModule getInstance(XController driver, XController manipulator) {
        if (INSTANCE == null) INSTANCE = new OperatorInputModule(driver, manipulator);
        return INSTANCE;
    }

    // private XController driverController, manipulatorController;

    private OperatorInputModule(XController driverController, XController manipulatorController) {
        // this.driverController = driverController;
        // this.manipulatorController = manipulatorController;

        // driverController.logInputsToShuffleboard();
        // manipulatorController.logInputsToShuffleboard();
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {
        // if (Robot.isReal()) return; // Only log below values in simulation
        // driverController.logButtonStatusToSmartdashboard(Constants.INPUT.DRIVER_SHUFFLEBOARD_TAB, true);
        // manipulatorController.logButtonStatusToSmartdashboard(Constants.INPUT.MANIPULATOR_SHUFFLEBOARD_TAB, true);
    }
}