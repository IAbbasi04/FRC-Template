package frc.robot.autonomous.autos.center;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class Center3WingAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("Center3WingAuto");
    }
}