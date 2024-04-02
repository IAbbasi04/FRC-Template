package frc.robot.autonomous.newtonCommands;

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
        return this.commandTimer.advanceIfElapsed(delayTime); // Always return true since this is mostly a passthrough command
    }

    @Override
    public void end(boolean interrupted) {}
}
