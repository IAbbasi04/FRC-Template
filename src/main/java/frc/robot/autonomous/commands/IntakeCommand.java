package frc.robot.autonomous.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeCommand extends Command {
    @Override
    public void initialize() {
        SmartDashboard.putBoolean("INTAKING", true);
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void shutdown() {
        SmartDashboard.putBoolean("INTAKING", false);
    }
}