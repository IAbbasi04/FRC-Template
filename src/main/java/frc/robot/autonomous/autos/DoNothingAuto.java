package frc.robot.autonomous.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;
import frc.robot.autonomous.commands.DelayCommand;

public class DoNothingAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new DelayCommand(1.0);
    }

    @Override
    public Pose2d getStartPose() {
        return new Pose2d();
    }
}
