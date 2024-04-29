package frc.robot.autonomous.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class ConditionalCommand extends NewtonCommand {
    private Command trueCommand, falseCommand;
    private Supplier<Boolean> condition;
    public ConditionalCommand(Command commandIfTrue, Command commandIfFalse, Supplier<Boolean> condition) {
        this.trueCommand = commandIfTrue;
        this.falseCommand = commandIfFalse;
        this.condition = condition;
    }

    @Override
    public void initialize() {
        if (condition.get()) { // Condition is true
            trueCommand.initialize();
        } else { // Condition is false
            falseCommand.initialize();
        }
    }

    @Override
    public void execute() {
        if (condition.get()) { // Condition is true
            trueCommand.execute();
        } else { // Condition is false
            falseCommand.execute();
        }
    }

    @Override
    public boolean isFinished() {
        if (condition.get()) { // Condition is true
            return trueCommand.isFinished();
        } else { // Condition is false
            return falseCommand.isFinished();
        }
    }
    
    @Override
    public void end(boolean interrupted) {
        if (condition.get()) { // Condition is true
            trueCommand.end(interrupted);
        } else { // Condition is false
            falseCommand.end(interrupted);
        }
    }
}