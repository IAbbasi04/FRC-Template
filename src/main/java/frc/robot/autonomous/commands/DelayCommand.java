package frc.robot.autonomous.commands;

public class DelayCommand extends NewtonCommand {
    private double delayTime;

    public DelayCommand(double delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public void initialize() {
        this.commandTimer.start();
    }

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return this.commandTimer.advanceIfElapsed(delayTime);
    }

    @Override
    public void end(boolean interrupted) {}
}
