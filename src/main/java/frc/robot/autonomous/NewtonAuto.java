package frc.robot.autonomous;

import com.pathplanner.lib.auto.*;
import com.pathplanner.lib.util.*;
import com.pathplanner.lib.commands.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.autonomous.commands.*;
import frc.robot.common.Constants;
import frc.robot.subsystems.SwerveSubsystem;

public abstract class NewtonAuto {
    /**
     * Prepares the auto builder for autonomous and command creation
     */
    public static void initializeAutoBuilder() {
        AutoBuilder.configureHolonomic(
            SwerveSubsystem.getInstance()::getCurrentPose, 
            SwerveSubsystem.getInstance()::setStartPose,
            SwerveSubsystem.getInstance()::getCurrentSpeeds, 
            SwerveSubsystem.getInstance()::drive,
            new HolonomicPathFollowerConfig(
                Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND,
                Constants.SWERVE.DRIVE_TRAIN_RADIUS,
                new ReplanningConfig()
            ),
            () -> {
                if (DriverStation.getAlliance().isPresent()) {
                    return DriverStation.getAlliance().get() == Alliance.Red;
                }
                return false; // Never flip if driver station does not display alliance color
            },
            SwerveSubsystem.getInstance()
        );

        /**
         * The following named commands are event markers that get called in path planner and correspond to the following commands
         */
        NamedCommands.registerCommand("Pause", new DelayCommand(1.0));
        // NamedCommands.registerCommand("Intake", new DelayCommand(1.0));
        // NamedCommands.registerCommand("RangeShoot", new DelayCommand(1.0));
        // NamedCommands.registerCommand("SubwooferShoot", new DelayCommand(0.75));
    }

    /**
     * Initializes and creates the autonomous routine; Used for passing into the WPILib command scheduler
     */
    public abstract Command createAuto();

    /**
     * Gets the selected auto
     */
    public static Command getSelected() {
        return new PathPlannerAuto("Center1WingAuto");
    }

    /**
     * The starting position of the robot for this particular auto
     */
    public abstract Pose2d getStartPose();
}