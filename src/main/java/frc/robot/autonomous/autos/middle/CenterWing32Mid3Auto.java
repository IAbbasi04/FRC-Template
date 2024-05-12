package frc.robot.autonomous.autos.middle;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class CenterWing32Mid3Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("CenterWing32Mid3Auto");
    }
}