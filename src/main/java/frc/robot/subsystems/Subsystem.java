package frc.robot.subsystems;

import lib.frc8592.MatchMode;
import lib.frc8592.logging.SmartLogger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Subsystem extends SubsystemBase {
    protected SmartLogger logger = new SmartLogger("");

    /**
     * Periodically runs and updates the shuffleboard outputs
     */
    public void updateLogger() {
        this.logger.update();
    }

    /**
     * Either turns on or off the logging for the particular subsystem
     */
    public void enableLogger(boolean log) {
        if (log) {
            logger.enable();
        } else {
            logger.disable();
        }
    }

    /**
     * Runs at the start of the current match mode
     */
    public abstract void init(MatchMode mode);

    /**
     * Sets the values that will be sent to the logger
     */
    public abstract void initializeLogs();
    
    /**
     * Runs throughout the entirety of the current match mode
     */
    public abstract void periodic();
}