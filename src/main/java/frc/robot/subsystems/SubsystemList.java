package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import lib.frc8592.MatchMode;

public class SubsystemList {
    private List<Subsystem> modules;

    /**
     * List of active {@code Modules} to do operations with and on
     */
    public SubsystemList(List<Subsystem> modules) {
        this.modules = modules;
    }

    /**
     * Adds a {@code Module} to the module list
     */
    public SubsystemList addModule(Subsystem module) {
        if (!modules.contains(module)) {
            this.modules.add(module);
        }
        return this;
    }

    /**
     * Removes a {@code Module} from the module list
     */
    public SubsystemList removeModule(Subsystem module) {
        if (modules.contains(module)) {
            modules.remove(module);
        }
        return this;
    }

    /**
     * Initializes all {@code Modules} in the module list
     * @param mode current match mode
     */
    public void initAll(MatchMode mode) {
        modules.forEach(module -> module.init(mode));
        modules.forEach(module -> module.initializeLogs());
    }

    /**
     * Runs periodic for all {@code Modules} in the module list
     */
    public void periodicAll() {
        modules.forEach(module -> module.periodic());
        modules.forEach(module -> module.updateLogger());
    }

    /**
     * Clears all active modules from list
     */
    public SubsystemList clearModules() {
        modules = new ArrayList<>();
        return this;
    }
}
