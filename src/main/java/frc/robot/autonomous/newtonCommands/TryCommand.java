package frc.robot.autonomous.newtonCommands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class TryCommand extends NewtonCommand {
    private Command command;
    private double tryTime = Double.NaN;
    private Supplier<Boolean> condition = () -> false;

    public TryCommand(Command commandToTry, Supplier<Boolean> condition) {
        this.command = commandToTry;
        this.condition = condition;
    }

    public TryCommand(Command commandToTry, double time) {
        this.command = commandToTry;
        this.tryTime = time;
    }

    @Override
    public void initialize() {
        this.command.initialize();
    }

    @Override
    public void execute() {
        this.command.execute();
    }

    @Override
    public boolean isFinished() {
        if (!Double.isNaN(tryTime)) { // Try time set
            return commandTimer.get() >= tryTime || command.isFinished();
        }
        return condition.get() || command.isFinished();
    }
    
    @Override
    public void end(boolean interrupted) {
        this.command.end(interrupted);
    }
}