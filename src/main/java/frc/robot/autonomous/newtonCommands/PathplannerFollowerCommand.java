package frc.robot.autonomous.newtonCommands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.modules.DriveModule;

public class PathplannerFollowerCommand extends NewtonCommand {
    private PathPlannerPath trajectory;
    private Command pathCommand;

    public PathplannerFollowerCommand(PathPlannerPath trajectory) {
        this.trajectory = trajectory;
        this.pathCommand = AutoBuilder.followPath(trajectory);
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
        return trajectory.getPreviewStartingHolonomicPose();
    }

    @Override
    public void end(boolean interrupted) {
        DriveModule.getInstance().drive(new ChassisSpeeds());
    }
}