package frc.robot.autonomous.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

public class PathFinderCommand extends NewtonCommand {
    private Command pathFinder;

    public PathFinderCommand(Pose2d targetPose, double maxVel, double maxAccel, double endVelocity, double rotationDelayDistance) {
        pathFinder = AutoBuilder.pathfindToPose(targetPose, new PathConstraints(maxVel, maxAccel, maxVel, maxAccel), endVelocity, rotationDelayDistance);
    }

    @Override
    public void initialize() {
        pathFinder.initialize();
    }

    @Override
    public void execute() {
        pathFinder.execute();
    }

    @Override
    public void end(boolean interrupted) {
        pathFinder.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return pathFinder.isFinished();
    }
}