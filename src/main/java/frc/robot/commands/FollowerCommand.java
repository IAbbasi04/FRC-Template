package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Robot;
import frc.robot.autonomous.SwerveTrajectory;

public class FollowerCommand extends Command {
    private SwerveTrajectory trajectory;

    public FollowerCommand(SwerveTrajectory trajectory) {
        this.trajectory = trajectory;
    }

    @Override
    public void initialize() {
        commandTimer.reset();
        commandTimer.start();
    }

    @Override
    public boolean execute() {
        if (!Robot.isReal()) {
        
        }
        
        return true;
    }

    @Override
    public Pose2d getStartPose() {
        return trajectory.getInitialPose();
    }

    @Override
    public void shutdown() {
        
    }
}