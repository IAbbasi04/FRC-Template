package frc.robot.autonomous.commands;

import commands.NewtonCommand;
import frc.robot.Robot;

public class IntakeCommand extends NewtonCommand {
    public IntakeCommand() {}

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