package frc.robot.autonomous.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.modules.DriveModule;

public class FollowerCommand extends Command {
    private SwerveTrajectory trajectory;

    public FollowerCommand(SwerveTrajectory trajectory) {
        this.trajectory = trajectory;
    }

    @Override
    public void initialize() {}

    @Override
    public boolean execute() {
        DriveModule.getInstance().drive(trajectory.sample(commandTimer.get()));
        return trajectory.isFinished(commandTimer.get());
    }

    @Override
    public Pose2d getStartPose() {
        return trajectory.getInitialPose();
    }

    @Override
    public void shutdown() {
        DriveModule.getInstance().drive(new ChassisSpeeds());
    }
}