package frc.robot.autonomous.commands;

import frc.robot.subsystems.Superstructure;

public class IntakeCommand extends NewtonCommand {
    @Override
    public void execute() {
        Superstructure.setIntaking();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Superstructure.setIntakeGroundState();
    }
}