package frc.robot.autonomous.commands;

import commands.NewtonCommand;
import frc.robot.Robot;

public class NewConditionalCommand extends NewtonCommand {
    public NewConditionalCommand() {}

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return Robot.isSimulation();
    }

    @Override
    public void end(boolean interrupted) {

    }
}
