package frc.robot.autonomous.commands;

import java.util.ArrayList;
import java.util.List;

public class RaceJointCommand extends ParallelJointCommand {
    private List<NewtonCommand> commands;

    public RaceJointCommand(NewtonCommand... commands) {
        this.commands = new ArrayList<>();
        for (NewtonCommand command : commands) {
            this.commands.add(command);
        }
    }

    @Override
    public boolean isFinished() {
        for (NewtonCommand command : commands) {
            if (command.isFinished()) return true;
        }
        return false;
    }
}