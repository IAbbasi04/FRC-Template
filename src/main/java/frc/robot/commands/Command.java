package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;

public abstract class Command {
    protected String tag = "DEFAULT COMMAND";
    protected double startDelay = 0.0;
    private Timer timeoutTimer = new Timer();
    protected Timer commandTimer = new Timer();
    public double timeout = 15.0; // 15 seconds means it technically never times out in autonomous

    /**
     * Setup the command; called directly before the command is run
     */
    public abstract void initialize();

    /**
     * Ran periodically for the command
     * @return whether or not the command has finished
     */
    public abstract boolean execute();

    /**
     * Ran after the command has finished; typically for turning off or resetting a mechanism
     */
    public abstract void shutdown();

    /**
     * Adds a delay to the start of the command
     */
    public Command addDelay(double delay) {
        this.startDelay = delay;
        return this;
    }

    /**
     * Adds a mandatory timeout to the command to end early
     */
    public Command addTimeout(double timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Starts the timer for the timeout
     */
    public Command startTimeout() {
        this.timeoutTimer.reset();
        this.timeoutTimer.start();
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
    public Command setTag(String tag) {
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