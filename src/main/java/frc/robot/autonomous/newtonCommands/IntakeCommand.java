package frc.robot.autonomous.newtonCommands;

import frc.robot.Robot;
import frc.robot.modes.ModeManager;
import frc.robot.modules.FeederModule;

public class IntakeCommand extends NewtonCommand {
    public IntakeCommand() {}

    @Override
    public void execute() {
        ModeManager.setIntaking();
    }

    @Override
    public boolean isFinished() {
        return FeederModule.getInstance().noteStaged() || Robot.isSimulation();
    }

    @Override
    public void end(boolean interrupted) {
        ModeManager.stopIntake();
    }
}