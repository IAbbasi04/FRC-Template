package frc.robot;

import java.util.List;

import org.littletonrobotics.junction.LoggedRobot;

import lib.frc8592.hardware.Clock;
import lib.frc8592.MatchMode;

import frc.robot.autonomous.*;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.tables.DefendedShotTable;
import frc.robot.common.crescendo.tables.ShotTable;
import frc.robot.common.crescendo.tables.UndefendedShotTable;
import frc.robot.modes.*;
import frc.robot.subsystems.*;
import frc.unittest.*;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;

public class Robot extends LoggedRobot {
  private SubsystemList activeModules;
  private ModeManager currentMode;

  public static boolean LOG_TO_DASHBOARD = true;

  public static MatchMode MODE = MatchMode.DISABLED;
  public static Field2d FIELD = new Field2d();
  public static Clock CLOCK = new Clock();

  private AutonomousSelector autoSelector;
  
  public static ShotTable UNDEFENDED_SHOT_TABLE = new UndefendedShotTable();
  public static ShotTable DEFENDED_SHOT_TABLE = new DefendedShotTable();

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
    
    autoSelector = new AutonomousSelector(); // Initialized here to allow auto selection during disabled mode

    NewtonAuto.initializeAutoBuilder(); // Sets up the pathplanner auto creation tool
  }

  @Override
  public void robotPeriodic() {
    if (MODE != MatchMode.AUTONOMOUS && MODE != MatchMode.TEST) { 
      // Autonomous uses WPILib command base 
      // so we do not want to run periodic
      //
      // Also experimenting with running only 
      // unit tests in test mode
      currentMode.runPeriodic();
    }
    
    // We do not want to log to shuffleboard if we are in a match 
    // since it will cause unnecessary noise
    Robot.LOG_TO_DASHBOARD = Robot.isSimulation() || DriverStation.isFMSAttached(); 

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
    Command autoCommand = autoSelector.getSelectedAutonomous().createAuto();

    // Set the starting position of the robot on the field
    Pose2d robotStartPose = autoSelector.getSelectedAutonomous().getStartPose();
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