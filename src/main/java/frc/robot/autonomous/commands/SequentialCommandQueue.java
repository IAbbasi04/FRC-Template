package frc.robot.autonomous.commands;

import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Robot;

/**
 * Running a queue extending off NewtonCommand allows us to use our helpful stuff from NewtonCommand.java
 */
public class SequentialCommandQueue extends NewtonCommand {
    private Queue<NewtonCommand> commands;
    private NewtonCommand currentCommand;
    private Pose2d startPose = null;

    /**
     * Note: Only works with Newton Commands
     */
    public SequentialCommandQueue(NewtonCommand... commands) {
        this.commands = new LinkedList<>();
        for (NewtonCommand command : commands) {
            this.commands.add(command);
            if (!command.getStartPose().equals(new Pose2d()) && this.startPose == null) {
                this.startPose = command.getStartPose();
            }
        }
    }

    /**
     * Moves onto the next command and preps it
     */
    private void resetPeek() {
        currentCommand = commands.poll();
        currentCommand.initialize();
        currentCommand.startCommandTimer();
        currentCommand.startTimeout();
    }

    @Override
    public void initialize() {
        resetPeek();
        Robot.FIELD.setRobotPose(startPose);
    }

    @Override
    public void execute() {
        if (currentCommand.isFinished() || currentCommand.isPastTimeout()) {
            // If either the command finishes or the timeout time passes
            currentCommand.end(false);
            if (commands.size() != 0) {
                this.resetPeek(); // Runs all steps necessary to start next command
            }
        } else if (currentCommand.isPastDelay()) { // Do not run command until the delay time has elapsed
            currentCommand.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return commands.size() == 0 && currentCommand.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        currentCommand.end(interrupted);
    }
}