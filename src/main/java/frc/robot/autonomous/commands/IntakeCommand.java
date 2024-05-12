package frc.robot.autonomous.commands;

import frc.robot.modes.ModeManager;

public class IntakeCommand extends NewtonCommand {
    public IntakeCommand() {}
    
    @Override
    public void execute() {
        ModeManager.setIntaking();
    }

    @Override
    public boolean isFinished() {
        // return commandTimer.get() >= 15.0;
        // return Robot.isSimulation();
        return false; // Always running with another command so should not end until other command ends
    }

    @Override
    public void end(boolean interrupted) {
        ModeManager.stopIntake();
        ModeManager.stopFeeder();
    }
}