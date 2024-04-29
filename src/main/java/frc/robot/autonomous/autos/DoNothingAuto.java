package frc.robot.autonomous.autos;

import commands.DelayCommand;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.NewtonAuto;

public class DoNothingAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new DelayCommand(1.0);
    }
}
