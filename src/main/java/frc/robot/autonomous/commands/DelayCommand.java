package frc.robot.autonomous.commands;

public class DelayCommand extends Command {
    private double delay;

    public DelayCommand(double delay) {
        this.delay = delay;
    }

    @Override
    public void initialize() {
        commandTimer.reset();
        commandTimer.start();
    }

    @Override
    public boolean execute() {
        return commandTimer.get() >= delay;
    }


    @Override
    public void shutdown() {
        commandTimer.stop();
        commandTimer.reset();
    }
}