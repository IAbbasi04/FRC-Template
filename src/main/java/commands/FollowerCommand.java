package commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.subsystems.SwerveSubsystem;

public class FollowerCommand extends NewtonCommand {
    private SwerveTrajectory trajectory;

    public FollowerCommand(SwerveTrajectory trajectory) {
        this.trajectory = trajectory;
    }

    @Override
    public void execute() {
        SwerveSubsystem.getInstance().drive(trajectory.sample(commandTimer.get()));
    }

    @Override
    public boolean isFinished() {
        return trajectory.isFinished(commandTimer.get());
    }

    @Override
    public void end(boolean interrupted) {
        SwerveSubsystem.getInstance().drive(new ChassisSpeeds());
    }

    @Override
    public Pose2d getStartPose() {
        return trajectory.getInitialPose();
    }
}