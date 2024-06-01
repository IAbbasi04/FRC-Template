package frc.robot.autonomous.autos.amp;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class AmpSideWing1Mid12Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("AmpSideWing1Mid12Auto");
    }

    @Override
    public Pose2d getStartPose() {
        return PathPlannerAuto.getStaringPoseFromAutoFile("AmpSideWing1Mid12Auto");
    }
}