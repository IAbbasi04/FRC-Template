package frc.robot.autonomous.commands;

import frc.robot.Robot;
import frc.robot.modes.ModeManager;
import frc.robot.modules.FeederModule;

public class IntakeCommand extends Command {
    public IntakeCommand() {}

    @Override
    public void initialize() {}

    @Override
    public boolean execute() {
        ModeManager.setIntaking();
        return FeederModule.getInstance().noteStaged() || Robot.isSimulation();
    }

    @Override
    public void shutdown() {
        ModeManager.stopIntake();
    }
}