package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import com.lib.team8592.controls.xbox.XboxController;

public class OperatorInputModule extends Module {
    private static OperatorInputModule INSTANCE = null;
    public static OperatorInputModule getInstance(XboxController driver, XboxController manipulator) {
        if (INSTANCE == null) INSTANCE = new OperatorInputModule(driver, manipulator);
        return INSTANCE;
    }

    private XboxController driverController, manipulatorController;

    private OperatorInputModule(XboxController driverController, XboxController manipulatorController) {
        this.driverController = driverController;
        this.manipulatorController = manipulatorController;

        driverController.logInputsToShuffleboard();
        manipulatorController.logInputsToShuffleboard();
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {
        if (Robot.isReal()) return; // Only log below values in simulation
        driverController.logButtonStatusToSmartdashboard(Constants.INPUT.DRIVER_SHUFFLEBOARD_TAB, true);
        manipulatorController.logButtonStatusToSmartdashboard(Constants.INPUT.MANIPULATOR_SHUFFLEBOARD_TAB, true);
    }
}