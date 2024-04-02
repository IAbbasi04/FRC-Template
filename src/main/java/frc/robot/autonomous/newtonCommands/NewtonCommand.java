package frc.robot.autonomous.newtonCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public abstract class NewtonCommand extends Command {
    protected String tag = "DEFAULT COMMAND";
    protected double startDelay = 0.0;
    private Timer timeoutTimer = new Timer();
    protected Timer commandTimer = new Timer();
    public double timeout = 15.0; // 15 seconds means it technically never times out in autonomous

    /**
     * Adds a delay to the start of the command
     */
    public NewtonCommand addDelay(double delay) {
        this.startDelay = delay;
        return this;
    }

    /**
     * Gets the delay for the start of the command
     */
    public double getDelay() {
        return this.startDelay;
    }

    /**
     * Adds a mandatory timeout to the command to end early
     */
    public NewtonCommand addTimeout(double timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Starts the timer for the timeout
     */
    public NewtonCommand startTimeout() {
        this.timeoutTimer.reset();
        this.timeoutTimer.start();
        return this;
    }


    /**
     * Running clock during the execution of the command
     */
    public NewtonCommand startCommandTimer() {
        this.commandTimer.reset();
        this.commandTimer.start();
        return this;
    }

    /**
     * Whether or not the timeout timer has past the timeout time
     */
    public boolean isPastTimeout() {
        return this.timeoutTimer.get() >= this.timeout;
    }

    /**
     * Sets a traceable tag for the given command; useful for cherry-picking a
     * certain command out of the queue
     */
    public NewtonCommand setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * @return {@code String} representing the given tag for the command
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * Any command that changes the position or orientation of the robot should {@code @Override} this method
     * @return {@code Pose2d} representing the starting position of the robot
     */
    public Pose2d getStartPose() {
        return new Pose2d();
    }
}