package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomous.AutonomousSelector;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.common.crescendo.tables.DefendedShotTable;
import frc.robot.common.crescendo.tables.ShotTable;
import frc.robot.common.crescendo.tables.UndefendedShotTable;
import frc.robot.controls.XboxController;
import frc.robot.hardware.Clock;
import frc.robot.modes.DisabledModeManager;
import frc.robot.modes.ModeManager;
import frc.robot.modes.TeleopModeManager;
import frc.robot.modes.TestModeManager;
import frc.robot.modules.DriveModule;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.LEDModule;
import frc.robot.modules.ModuleList;
import frc.robot.modules.OperatorInputModule;
import frc.robot.modules.ShooterModule;

public class Robot extends TimedRobot {
  private ModuleList activeModules;
  private ModeManager currentMode;
  private XboxController driverController, operatorController;

  public static MatchMode MODE = MatchMode.DISABLED;;
  public static Field2d FIELD = new Field2d();
  public static Clock CLOCK = new Clock();

  public static boolean LOG_TO_DASHBOARD = true;

  public static ShotTable UNDEFENDED_SHOT_TABLE = new UndefendedShotTable();
  public static ShotTable DEFENDED_SHOT_TABLE = new DefendedShotTable();

  private AutonomousSelector autoSelector;

  @Override
  public void robotInit() {
    // Controllers
    driverController = new XboxController(Constants.INPUT.DRIVER_CONTROLLER_PORT, "Driver");
    operatorController = new XboxController(Constants.INPUT.OPERATOR_CONTROLLER_PORT, "Manipulator");

    // Add all modules to run here
    activeModules = new ModuleList(List.of(
      OperatorInputModule.getInstance(driverController, operatorController),
      DriveModule.getInstance(),
      IntakeModule.getInstance(),
      FeederModule.getInstance(),
      ShooterModule.getInstance(),
      LEDModule.getInstance()
    ));

    autoSelector = new AutonomousSelector();
  }

  @Override
  public void robotPeriodic() {
    currentMode.runPeriodic();
    activeModules.periodicAll();
    CLOCK.update();
  }

  @Override
  public void autonomousInit() {
    MODE = MatchMode.AUTONOMOUS;
    CLOCK.restart();
    BaseAuto selectedAuto = autoSelector.getSelectedAutonomous();
    selectedAuto.initialize();
    currentMode = selectedAuto;
    activeModules.initAll(MODE);
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    MODE = MatchMode.TELEOP;
    CLOCK.restart();
    currentMode = TeleopModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {
    MODE = MatchMode.DISABLED;
    CLOCK.restart();
    currentMode = DisabledModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    MODE = MatchMode.TEST;
    CLOCK.restart();
    currentMode = TestModeManager.getInstance();
    currentMode.setControllers(driverController, operatorController);
    activeModules.initAll(MODE);
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {
    SmartDashboard.putData(FIELD); // Only in simulation to prevent latency
  }

  @Override
  public void simulationPeriodic() {
    // Runs the mode manager for all other modes
  }
}