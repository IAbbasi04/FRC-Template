package frc.robot.autonomous.autos;

import frc.robot.autonomous.BaseAuto;
import frc.robot.commands.CommandQueue;
import frc.robot.commands.DelayCommand;

public class DoNothingAuto extends BaseAuto {
    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DelayCommand(1.0)
        );
    }
}
