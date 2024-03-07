package frc.robot.autonomous.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DeliverCommand extends Command {
    private DeliverType type;

    public enum DeliverType {
        AMP,
        SPEAKER,
        GROUND;
    }

    public DeliverCommand(DeliverType type) {
        this.type = type;
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("DELIVER MODE", type.toString());
    }

    @Override
    public boolean execute() {
        return commandTimer.get() >= 0.75;
    }

    @Override
    public void shutdown() {
        SmartDashboard.putString("DELIVER MODE", "N/A");
    }
    
}