package frc.robot.autonomous.autos.ampside;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Robot;
import frc.robot.autonomous.NewtonAuto;
import frc.robot.autonomous.newtonCommands.IntakeCommand;
import frc.robot.autonomous.newtonCommands.PathplannerFollowerCommand;
import frc.robot.autonomous.newtonCommands.ShootCommand;

public class AmpSide1WingAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new SequentialCommandGroup(
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getSubwooferShot(), false),
            new ParallelCommandGroup(
                new PathplannerFollowerCommand(PathPlannerPath.fromPathFile("Speaker Up to Wing Note 1")),
                new IntakeCommand()
            ),
            new ShootCommand(),
            new ParallelCommandGroup(
                new PathplannerFollowerCommand(PathPlannerPath.fromPathFile("Wing Note 1 to Wing Note 2")),
                new IntakeCommand()
            ),
            new ShootCommand()
        );
    }
}