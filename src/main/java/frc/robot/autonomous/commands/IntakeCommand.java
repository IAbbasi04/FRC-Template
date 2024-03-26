package frc.robot.autonomous.commands;

import frc.robot.modes.ModeManager;
import frc.robot.modules.FeederModule;

public class IntakeCommand extends Command {
    public IntakeCommand() {}

    @Override
    public void initialize() {}

    @Override
    public boolean execute() {
        ModeManager.setIntaking();
        return FeederModule.getInstance().noteStaged();
    }

    @Override
    public void shutdown() {
        ModeManager.stopAllRollers();
    }
}