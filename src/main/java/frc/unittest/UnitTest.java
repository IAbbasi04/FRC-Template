package frc.unittest;

import edu.wpi.first.wpilibj.Timer;

public abstract class UnitTest {
    protected Timer testTimer = new Timer();

    /**
     * Sets up everything needed to begin the testing sequence
     */
    public abstract void initialize();

    /**
     * Runs the testing sequence
     */
    public abstract void run();

    /**
     * Did not achieve desired end result
     */
    public abstract boolean hasFailed();

    /**
     * Achieved desired end result
     */
    public abstract boolean hasSucceeded();

    /**
     * Starts the timer
     */
    public void start() {
        this.testTimer.reset();
        this.testTimer.start();
    }

    /**
     * Current test is considered finished if it has passed or failed the requirements
     */
    public boolean hasFinished() {
        return hasFailed() || hasSucceeded();
    }
}