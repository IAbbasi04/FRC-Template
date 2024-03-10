package frc.robot.autonomous.commands;

public class JointCommand extends Command {
    private Command[] commands;

    public JointCommand(Command... pCommands) {
        commands = pCommands;
    }

    @Override
    public void initialize() {
        for (Command command : commands) {
            command.initialize();
        }
    }

    @Override
    public boolean execute() {
        boolean finished = true;
        for (Command command : commands) {
            if (!command.execute()) {
                finished = false;
            }
        }

        return finished;
    }

    /**
     * All commands running simultaneously in the joint command
     */
    public Command[] getCommands() {
        return commands;
    }

    @Override
    public void shutdown() {
        for (Command command : commands) {
            command.shutdown();
        }
    }
}