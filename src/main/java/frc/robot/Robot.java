package frc.robot;

import java.util.List;

import org.littletonrobotics.junction.LoggedRobot;

import com.lib.team8592.hardware.Clock;

import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.controls.xbox.XboxController;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.autonomous.*;
import frc.robot.modes.*;
import frc.robot.modules.*;

public class Robot extends LoggedRobot {
  private ModuleList activeModules;
  private ModeManager currentMode;
  private XboxController driverController, operatorController;

  public static boolean LOG_TO_DASHBOARD = true;

  public static MatchMode MODE = MatchMode.DISABLED;;
  public static Field2d FIELD = new Field2d();
  public static Clock CLOCK = new Clock();

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
      ExampleModule.getInstance()
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