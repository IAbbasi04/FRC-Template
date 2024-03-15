package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.controls.DriverProfile;
import frc.robot.controls.ManipulatorProfile;
import frc.robot.controls.XboxController;

public class OperatorInputModule extends Module {
    private static OperatorInputModule INSTANCE = null;
    public static OperatorInputModule getInstance(XboxController driver, XboxController manipulator) {
        if (INSTANCE == null) {
            INSTANCE = new OperatorInputModule(driver, manipulator);
        }
        return INSTANCE;
    }

    private XboxController driverController, manipulatorController;

    private OperatorInputModule(XboxController driverController, XboxController manipulatorController) {
        this.driverController = driverController;
        this.manipulatorController = manipulatorController;

        DriverProfile.logToSmartdashboard();
        ManipulatorProfile.logToSmartdashboard();

        driverController.logInputsToShuffleboard();
        manipulatorController.logInputsToShuffleboard();
    }

    @Override
    public void init(MatchMode mode) {
        DriverProfile.updateSelected();
        ManipulatorProfile.updateSelected();

        if (mode.isAny(MatchMode.TELEOP, MatchMode.TEST)) {
            DriverProfile.changeInputs();
            ManipulatorProfile.changeInputs();
        }
    }

    @Override
    public void periodic() {
        if (DriverProfile.getSelected() == DriverProfile.DEFAULT) {
            driverController.updateChanges();
        }

        if (ManipulatorProfile.getSelected() == ManipulatorProfile.DEFAULT) {
            manipulatorController.updateChanges();
        }

        if (Robot.isReal()) return; // Only log below values in simulation
        driverController.logButtonStatusToSmartdashboard(Constants.INPUT.DRIVER_SHUFFLEBOARD_TAB, true);
        manipulatorController.logButtonStatusToSmartdashboard(Constants.INPUT.MANIPULATOR_SHUFFLEBOARD_TAB, true);
    }
}