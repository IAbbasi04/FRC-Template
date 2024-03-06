package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomous.AutonomousSelector;
import frc.robot.autonomous.BaseAuto;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.controls.XboxController;
import frc.robot.modes.DisabledModeManager;
import frc.robot.modes.ModeManager;
import frc.robot.modes.TeleopModeManager;
import frc.robot.modes.TestModeManager;
import frc.robot.modules.DriveModule;
import frc.robot.modules.ModuleList;

public class Robot extends TimedRobot {
  private ModuleList activeModules;
  private ModeManager currentMode;
  public static XboxController driverController, operatorController;

  public static MatchMode MODE;
  public static Field2d FIELD;

  private AutonomousSelector autoSelector;

  @Override
  public void robotInit() {
    MODE = MatchMode.DISABLED;
    driverController = new XboxController(Constants.INPUT.DRIVER_CONTROLLER_PORT);
    operatorController = new XboxController(Constants.INPUT.OPERATOR_CONTROLLER_PORT);

    // Add all modules to run here
    activeModules = new ModuleList(List.of(
      DriveModule.getInstance()
    ));

    autoSelector = new AutonomousSelector();

    FIELD = new Field2d();
  }

  @Override
  public void robotPeriodic() {
    currentMode.runPeriodic();
  }

  @Override
  public void autonomousInit() {
    MODE = MatchMode.AUTONOMOUS;
    BaseAuto selectedAuto = autoSelector.getSelectedAutonomous();
    selectedAuto.initialize();
    currentMode = selectedAuto;
    activeModules.initAll(MODE);
  }

  @Override
  public void autonomousPeriodic() {
    activeModules.periodicAll();
  }

  @Override
  public void teleopInit() {
    MODE = MatchMode.TELEOP;
    currentMode = TeleopModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void teleopPeriodic() {
    activeModules.periodicAll();
  }

  @Override
  public void disabledInit() {
    MODE = MatchMode.DISABLED;
    currentMode = DisabledModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void disabledPeriodic() {
    activeModules.periodicAll();
  }

  @Override
  public void testInit() {
    MODE = MatchMode.TEST;
    currentMode = TestModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void testPeriodic() {
    activeModules.periodicAll();
  }

  @Override
  public void simulationInit() {
    SmartDashboard.putData(FIELD); // Only in simulation to prevent latency
  }

  @Override
  public void simulationPeriodic() {
    // Runs the mode manager for all other modes
  }
}