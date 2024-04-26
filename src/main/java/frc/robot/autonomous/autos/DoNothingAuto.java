package frc.robot.autonomous.autos;

import com.lib.team8592.autonomous.NewtonAuto;
import com.lib.team8592.commands.DelayCommand;

import edu.wpi.first.wpilibj2.command.Command;

public class DoNothingAuto extends NewtonAuto {
    @Override
    public Command createAuto() {
        return new DelayCommand(1.0);
    }
}
