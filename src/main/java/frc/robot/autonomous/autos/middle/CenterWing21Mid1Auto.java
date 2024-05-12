package frc.robot.autonomous.autos.middle;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.autonomous.NewtonAuto;

public class CenterWing21Mid1Auto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new SequentialCommandGroup(
            new PathPlannerAuto("CenterWing21Mid1Auto")
        );
    }
}