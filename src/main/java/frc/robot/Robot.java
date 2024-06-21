package frc.robot;

import java.util.List;

import org.littletonrobotics.junction.LoggedRobot;

import lib.frc8592.hardware.Clock;
import lib.frc8592.MatchMode;

import frc.robot.autonomous.*;
import frc.robot.common.Constants;
import frc.robot.common.Controls;
import frc.robot.crescendo.tables.ShotTable;
import frc.robot.modes.*;
import frc.robot.subsystems.*;
import frc.unittest.*;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;

public class Robot extends LoggedRobot {
  private SubsystemList activeModules;
  private ModeManager currentMode;
  // private AutonomousSelector autoSelector;
  private AutonomousBuilder autoBuilder;

  public static MatchMode MODE = MatchMode.DISABLED;
  public static Field2d FIELD = new Field2d();
  public static Clock CLOCK = new Clock();
  public static Controls CONTROLS = new Controls();
  public static ShotTable SHOT_TABLE = new ShotTable();
  
  public static boolean LOG_TO_DASHBOARD = true;

  @Override
  public void robotInit() {        
    // Add all modules to run here
    activeModules = new SubsystemList(List.of(
      LoggerSubsystem.getInstance(),
      PowerSubsystem.getInstance(),
      VisionSubsystem.getInstance(),
      LEDSubsystem.getInstance(),
      SwerveSubsystem.getInstance(),
      IntakeSubsystem.getInstance(),
      ShooterSubsystem.getInstance(),
      ElevatorSubsystem.getInstance(),
      FeederSubsystem.getInstance()
    ));
    
    // autoSelector = new AutonomousSelector(); // Initialized here to allow auto selection during disabled mode
    autoBuilder = new AutonomousBuilder();

    NewtonAuto.initializeAutoBuilder(); // Sets up the pathplanner auto creation tool
  }

  @Override
  public void robotPeriodic() {
    if (MODE != MatchMode.AUTONOMOUS && MODE != MatchMode.TEST) { 
      // Updates the controls and what the status of all buttons are
      // Currently set to single driver controls
      CONTROLS.updateSingleDriver();

      // Autonomous uses WPILib command base 
      // so we do not want to run periodic
      //
      // Also experimenting with running only 
      // unit tests in test mode
      currentMode.runPeriodic();
    }
    
    // We do not want to log to shuffleboard if we are in a match 
    // since it will cause unnecessary noise
    // Robot.LOG_TO_DASHBOARD = Robot.isSimulation() || !DriverStation.isFMSAttached(); 

    activeModules.periodicAll(); // Always calls regardless of match mode
    CLOCK.update();

    // Used mainly to run autonoumous routine
    // Also used for 'on the fly' swerve pathing and test plan sequence
    CommandScheduler.getInstance().run(); 
  }

  @Override
  public void autonomousInit() {
    MODE = MatchMode.AUTONOMOUS;
    CLOCK.restart();

    activeModules.initAll(MODE);

    // The entire autonomous routine gets compiled into a WPILib Command.java class
    // This makes it so that only a single command needs to get scheduled to run the
    // entire autonomous
    // Command autoCommand = autoSelector.getSelectedAutonomous().createAuto();
    Command autoCommand = autoBuilder.buildAuto();

    // Set the starting position of the robot on the field
    // Pose2d robotStartPose = autoSelector.getSelectedAutonomous().getStartPose();
    Pose2d robotStartPose = autoBuilder.getStartPose();

    if (DriverStation.getAlliance().get() == Alliance.Red) {
      robotStartPose = new Pose2d(
        new Translation2d(
          Constants.FIELD.RED_WALL_X - robotStartPose.getX(),
          robotStartPose.getY()
        ),
        Rotation2d.fromDegrees(
          180 - robotStartPose.getRotation().getDegrees()
        )
      );
    }
    
    SwerveSubsystem.getInstance().setStartPose(robotStartPose);

    if (autoCommand != null) { // If somehow no auto selected do not run anything
      autoCommand.schedule(); // Scheduling a command just throws it into a 'queue' to be ran by WPILib
    }
  }

  @Override
  public void autonomousPeriodic() {
    // Since everything is being handled in the backend, nothing needs to be called here
  }

  @Override
  public void teleopInit() {
    MODE = MatchMode.TELEOP;
    CLOCK.restart();
    currentMode = TeleopModeManager.getInstance();
    currentMode.activateControllers();
    activeModules.initAll(MODE);
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void teleopPeriodic() {
    // Since everything is ran through the mode manager, nothing is needed here
  }

  @Override
  public void disabledInit() {
    MODE = MatchMode.DISABLED;
    CLOCK.restart();
    currentMode = DisabledModeManager.getInstance();
    currentMode.activateControllers();
    activeModules.initAll(MODE);
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void disabledPeriodic() {
    // Since everything is ran through the mode manager, nothing is needed here
  }

  @Override
  public void testInit() {
    MODE = MatchMode.TEST;
    CLOCK.restart();
    currentMode = TestModeManager.getInstance();
    currentMode.activateControllers();
    activeModules.initAll(MODE);

    CommandScheduler.getInstance().cancelAll();
    new UnitTestSequence(
      List.of(
        new SwerveUnitTest()
      )
    ).schedule();
  }

  @Override
  public void testPeriodic() {
    // Since everything is ran through the mode manager, nothing is needed here
  }

  @Override
  public void simulationInit() {
    SmartDashboard.putData(FIELD); // Only in simulation to prevent latency
  }

  @Override
  public void simulationPeriodic() {
    // Runs the mode manager for all other modes
    // No other code required here
  }
}