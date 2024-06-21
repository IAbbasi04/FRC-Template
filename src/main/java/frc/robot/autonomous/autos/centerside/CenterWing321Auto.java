package frc.robot.autonomous.autos.centerside;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class CenterWing321Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("CenterWing321Auto");
    }

    @Override
    public Pose2d getStartPose() {
        return PathPlannerAuto.getStaringPoseFromAutoFile("CenterWing321Auto");
    }
}