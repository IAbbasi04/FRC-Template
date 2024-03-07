package frc.robot.autonomous.autos;

import frc.robot.autonomous.commands.CommandQueue;
import frc.robot.autonomous.commands.DelayCommand;

public class DoNothingAuto extends BaseAuto {
    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DelayCommand(1.0)
        );
    }
}
