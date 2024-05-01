package frc.robot.autonomous.commands;

import java.util.ArrayList;
import java.util.List;

public class JointCommand extends NewtonCommand {
    private List<NewtonCommand> commands;

    public JointCommand(NewtonCommand... commands) {
        this.commands = new ArrayList<>();
        for (NewtonCommand command : commands) {
            this.commands.add(command);
        }
    }

    @Override
    public void initialize() {
        commands.forEach(c -> c.initialize());
    }

    @Override
    public void execute() {
        commands.forEach(c -> c.execute());
    }

    @Override
    public boolean isFinished() {
        for (NewtonCommand command : commands) {
            if (!command.isFinished()) return false;
        }
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        commands.forEach(c -> c.end(interrupted));
    }
}
