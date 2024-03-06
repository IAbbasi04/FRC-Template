package frc.robot.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.modes.ModeManager;

public abstract class BaseAuto extends ModeManager {
    @Override
    public void runPeriodic() {

    }

    /**
     * The starting position of the robot in auto
     */
    public abstract Pose2d getStartPose();
}