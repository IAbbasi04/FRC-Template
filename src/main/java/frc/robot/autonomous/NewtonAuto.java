package frc.robot.autonomous;

import com.pathplanner.lib.auto.*;
import com.pathplanner.lib.util.*;
import com.pathplanner.lib.commands.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Robot;
import frc.robot.autonomous.newtonCommands.*;

import frc.robot.common.Constants;
import frc.robot.modules.DriveModule;

public abstract class NewtonAuto {
    public static void initializeAutoBuilder() {
        AutoBuilder.configureHolonomic(
            DriveModule.getInstance()::getCurrentPose, 
            DriveModule.getInstance()::setStartPose,
            DriveModule.getInstance()::getCurrentSpeed, 
            DriveModule.getInstance()::drive,
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
            DriveModule.getInstance()
        );

        /**
         * The following named commands are event markers that get called in path planner and correspond to the following commands
         */
        NamedCommands.registerCommand("Intake", new IntakeCommand());
        NamedCommands.registerCommand("SubwooferShoot", new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getSubwooferShot(), false));
        NamedCommands.registerCommand("RangeShoot", new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getStaticShot(), true));
        NamedCommands.registerCommand("RangePrime", new PrimeCommand());
    }

    /**
     * Useful for passing into the WPILib command scheduler; Initializes and creates the autonomous routine
     */
    public abstract Command createAuto();

    /**
     * Gets the selected auto
     */
    public static Command getSelected() {
        return new PathPlannerAuto("Center1WingAuto");
    }
}