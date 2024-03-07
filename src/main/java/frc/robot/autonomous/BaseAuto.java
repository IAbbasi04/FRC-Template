package frc.robot.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import frc.robot.autonomous.commands.CommandQueue;
import frc.robot.modes.ModeManager;

public abstract class BaseAuto extends ModeManager {
    protected CommandQueue queue; // The queue of commands to run in autonomous

    protected TrajectoryConfig slowConfig = new TrajectoryConfig(1.0, 1.0);
    protected TrajectoryConfig fastConfig = new TrajectoryConfig(4.0, 3.0);
    protected TrajectoryConfig movingScoreConfig = new TrajectoryConfig(2.0, 2.0);

    /**
     * Prepares the queue of commands for the current auto
     */
    public abstract void initialize();

    @Override
    public void runPeriodic() {
        queue.run();
    }

    /**
     * The starting position of the robot in auto
     */
    public Pose2d getStartPose() {
        return queue.getStartPose();
    }
}