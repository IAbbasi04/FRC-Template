package frc.robot.autonomous.autos.ampside;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class AmpSide1Wing2MidAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new PathPlannerAuto("AmpSide1Wing2MidAuto");
    }
}