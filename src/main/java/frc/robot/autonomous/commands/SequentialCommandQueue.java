package frc.robot.autonomous.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Robot;
import frc.robot.common.Constants;

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
     * Note: Only works with Newton Commands
     */
    public SequentialCommandQueue(List<NewtonCommand> commands) {
        this.commands = new LinkedList<>();
        for (NewtonCommand command : commands) {
            this.commands.add(command);
            if (!command.getStartPose().equals(new Pose2d()) && this.startPose == null) {
                this.startPose = command.getStartPose();
            }
        }
    }

    /**
     * Sets where the robot starts in auto
     */
    public SequentialCommandQueue setStartPose(Pose2d startPose) {
        this.startPose = startPose;
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            this.startPose = new Pose2d(
                Constants.FIELD.RED_WALL_X - startPose.getX(),
                startPose.getY(),
                Rotation2d.fromDegrees(180).minus(startPose.getRotation())
            );
        }
        return this;
    }

    /**
     * Moves onto the next command and preps it
     */
    private void resetPeek() {
        currentCommand = this.commands.poll();
        currentCommand.initialize();
        currentCommand.startCommandTimer();
        currentCommand.startTimeout();
    }

    @Override
    public void initialize() {
        resetPeek();
        if (startPose == null) {
            startPose = new Pose2d();
        }
        Robot.FIELD.setRobotPose(startPose);
    }

    @Override
    public void execute() {
        boolean endConditionMet = false;
        if (currentCommand != null) {
            endConditionMet = endConditionMet || currentCommand.getEndCondition().get();
        }

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