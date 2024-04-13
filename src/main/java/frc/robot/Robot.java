package frc.robot;

import java.util.List;

import org.littletonrobotics.junction.LoggedRobot;

import com.lib.team8592.hardware.Clock;
import com.lib.team8592.logging.SmartLogger;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomous.AutonomousSelector;
import frc.robot.autonomous.NewtonAuto;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.common.crescendo.tables.DefendedShotTable;
import frc.robot.common.crescendo.tables.ShotTable;
import frc.robot.common.crescendo.tables.UndefendedShotTable;
import frc.robot.controls.xbox.XboxController;
import frc.robot.modes.DisabledModeManager;
import frc.robot.modes.ModeManager;
import frc.robot.modes.TeleopModeManager;
import frc.robot.modes.TestModeManager;
import frc.robot.modules.DriveModule;
import frc.robot.modules.ElevatorModule;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.LEDModule;
import frc.robot.modules.LoggerModule;
import frc.robot.modules.ModuleList;
import frc.robot.modules.OperatorInputModule;
import frc.robot.modules.PowerModule;
import frc.robot.modules.ShooterModule;
import frc.robot.modules.VisionModule;

public class Robot extends LoggedRobot {
  private ModuleList activeModules;
  private ModeManager currentMode;
  private XboxController driverController, operatorController;

  public static boolean LOG_TO_DASHBOARD = true;

  public static MatchMode MODE = MatchMode.DISABLED;;
  public static Field2d FIELD = new Field2d();
  public static Clock CLOCK = new Clock();
  public static SmartLogger LOGGER = new SmartLogger("Robot", LOG_TO_DASHBOARD);


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
      LoggerModule.getInstance(),
      PowerModule.getInstance(),
      VisionModule.getInstance(),
      LEDModule.getInstance(),
      DriveModule.getInstance(),
      IntakeModule.getInstance(),
      FeederModule.getInstance(),
      ShooterModule.getInstance(),
      ElevatorModule.getInstance()
    ));

    autoSelector = new AutonomousSelector();
    NewtonAuto.initializeAutoBuilder();
  }

  @Override
  public void robotPeriodic() {
    if (MODE != MatchMode.AUTONOMOUS) {
      currentMode.runPeriodic();
    }
    
    activeModules.periodicAll();
    CLOCK.update();

    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    MODE = MatchMode.AUTONOMOUS;
    CLOCK.restart();

    activeModules.initAll(MODE);
    Command autoCommand = autoSelector.getSelectedAutonomous().createAuto();
    if (autoCommand != null) {
      autoCommand.schedule();
    }
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