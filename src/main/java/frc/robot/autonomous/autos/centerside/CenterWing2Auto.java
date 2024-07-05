package frc.robot.autonomous.autos.centerside;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.Robot;
import frc.robot.autonomous.*;
import frc.robot.autonomous.commands.crescendo.*;

public class CenterWing2Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new SequentialCommandGroup(
            new ScoreCommand(Robot.SHOT_TABLE.getSubwooferShot()), // Shot preload note
            new ParallelRaceGroup( // Intake second note
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("Center Speaker to Wing Note 2")),
                new IntakeCommand()
            ),
            new ScoreCommand() // Score second note
        );
    }

    @Override
    public Pose2d getStartPose() {
        return AutoGenerator.SUBWOOFER_MIDDLE.getPose();
    }
}