package frc.robot.autonomous.autos.sourceside;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class SourceSideMid54Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("SourceSideMid54Auto");
    }

    @Override
    public Pose2d getStartPose() {
        return PathPlannerAuto.getStaringPoseFromAutoFile("SourceSideMid54Auto");
    }
}