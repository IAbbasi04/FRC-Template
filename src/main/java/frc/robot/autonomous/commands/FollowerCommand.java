package frc.robot.autonomous.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.subsystems.SwerveSubsystem;

public class FollowerCommand extends NewtonCommand {
    private SwerveTrajectory trajectory;

    public FollowerCommand(SwerveTrajectory trajectory) {
        this.trajectory = trajectory;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if (isPastDelay()) {
            SwerveSubsystem.getInstance().drive(trajectory.sample(commandTimer.get() - startDelay));
            if (Robot.isSimulation()) {
                trajectory.simulate(commandTimer.get() - startDelay, false);
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (!isPastDelay()) return false;
        return trajectory.isFinished(commandTimer.get() - startDelay);
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