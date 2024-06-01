package frc.robot.autonomous.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class PathplannerFollowerCommand extends NewtonCommand {
    private Command pathCommand;
    private Pose2d startPose;

    public PathplannerFollowerCommand(PathPlannerPath trajectory) {
        this.pathCommand = AutoBuilder.followPath(trajectory);
        this.startPose = trajectory.getPreviewStartingHolonomicPose();
    }

    public PathplannerFollowerCommand(Command trajectoryCommand, Pose2d startPose) {
        this.pathCommand = trajectoryCommand;
        this.startPose = startPose;
    }

    @Override
    public void initialize() {
        pathCommand.initialize();
    }

    @Override
    public void execute() {
        pathCommand.execute();
    }

    @Override
    public boolean isFinished() {
        return pathCommand.isFinished();
    }

    @Override
    public Pose2d getStartPose() {
        return this.startPose;
    }

    @Override
    public void end(boolean interrupted) {
        SwerveSubsystem.getInstance().drive(new ChassisSpeeds());
    }
}