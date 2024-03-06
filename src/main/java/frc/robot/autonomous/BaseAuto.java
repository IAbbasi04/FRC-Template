package frc.robot.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.commands.CommandQueue;
import frc.robot.modes.ModeManager;

public abstract class BaseAuto extends ModeManager {
    private CommandQueue queue; // The queue of commands to run in autonomous

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