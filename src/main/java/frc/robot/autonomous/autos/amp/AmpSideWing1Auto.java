package frc.robot.autonomous.autos.amp;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.autonomous.NewtonAuto;
import frc.robot.autonomous.commands.IntakeCommand;
import frc.robot.autonomous.commands.ParallelJointCommand;
import frc.robot.autonomous.commands.PathplannerFollowerCommand;
import frc.robot.autonomous.commands.SequentialCommandQueue;
import frc.robot.autonomous.commands.ShootCommand;

public class AmpSideWing1Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new SequentialCommandQueue(
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getSubwooferShot(), false),
            new ParallelJointCommand(
                new IntakeCommand(),
                new PathplannerFollowerCommand(PathPlannerPath.fromPathFile("Speaker Up to Wing Note 1"))
            ),
            new ShootCommand()
        ).setStartPose(AutoGenerator.SUBWOOFER_AMP.getPose());
    }

    @Override
    public Pose2d getStartPose() {
        return AutoGenerator.SUBWOOFER_AMP.getPose();
    }
}