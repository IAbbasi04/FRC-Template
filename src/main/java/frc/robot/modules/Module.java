package frc.robot.modules;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.common.Enums.MatchMode;
import frc.robot.logging.SmartLogger;

public abstract class Module extends SubsystemBase {
    protected SmartLogger logger = new SmartLogger("SmartDashboard", true); // Originally starts at smartdashboard tab

    /**
     * Adds a logger with a specified tab name for displaying to smart dashboard
     */
    public void addLogger(String tabName, boolean logToSmartdashboard) {
        logger = new SmartLogger(tabName, logToSmartdashboard);
    }

    /**
     * Periodically runs and updates the shuffleboard outputs
     */
    public void updateLogger() {
        this.logger.update();
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