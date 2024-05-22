package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import frc.robot.Robot;
import lib.frc8592.MatchMode;

public class SubsystemList {
    private List<Subsystem> subsystems;

    /**
     * List of active {@code Modules} to do operations with and on
     */
    public SubsystemList(List<Subsystem> subsystems) {
        this.subsystems = subsystems;
    }

    /**
     * Adds a {@code Subsystems} to the subsystem list
     */
    public SubsystemList addModule(Subsystem subsystem) {
        if (!subsystems.contains(subsystem)) {
            this.subsystems.add(subsystem);
        }
        return this;
    }

    /**
     * Removes a {@code Subsystems} from the subsystem list
     */
    public SubsystemList removeModule(Subsystem subsystem) {
        if (subsystems.contains(subsystem)) {
            subsystems.remove(subsystem);
        }
        return this;
    }

    /**
     * Initializes all logs for the {@code Subsystems} in the subsystem list
     * @param mode current match mode
     */
    public void initAllLogs(boolean enableLogs) {
        subsystems.forEach(subsystem -> subsystem.initializeLogs());
        subsystems.forEach(subsystem -> subsystem.enableLogger(enableLogs));
    }

    /**
     * Initializes all {@code Subsystems} in the subsystem list
     * @param mode current match mode
     */
    public void initAll(MatchMode mode) {
        subsystems.forEach(subsystem -> subsystem.init(mode));
        initAllLogs(Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Runs periodic for all {@code Subsystems} in the subsystem list
     */
    public void periodicAll() {
        subsystems.forEach(subsystem -> subsystem.periodic());
        subsystems.forEach(subsystem -> subsystem.updateLogger());
    }

    /**
     * Clears all active subsystem from list
     */
    public SubsystemList clearSubsystems() {
        subsystems = new ArrayList<>();
        return this;
    }
}
