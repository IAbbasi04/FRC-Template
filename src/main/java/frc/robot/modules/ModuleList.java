package frc.robot.modules;

import java.util.List;

import frc.robot.common.Enums.MatchMode;

public class ModuleList {
    private List<Module> modules;

    /**
     * List of active {@code Modules} to do operations with and on
     */
    public ModuleList(List<Module> modules) {
        this.modules = modules;
    }

    /**
     * Adds a {@code Module} to the module list
     */
    public ModuleList addModule(Module module) {
        if (!modules.contains(module)) {
            modules.add(module);
        } else {
            throw new UnsupportedOperationException("ALREADY EXISTS IN LIST");
        }
        return this;
    }

    /**
     * Removes a {@code Module} from the module list
     */
    public ModuleList removeModule(Module module) {
        if (modules.contains(module)) {
            modules.remove(module);
        } else {
            throw new UnsupportedOperationException("DOES NOT EXIST IN LIST");
        }
        return this;
    }

    /**
     * Initializes all {@code Modules} in the module list
     * @param mode current match mode
     */
    public void initAll(MatchMode mode) {
        modules.forEach(module -> module.init(mode));
    }

    /**
     * Runs periodic for all {@code Modules} in the module list
     */
    public void periodicAll(MatchMode mode) {
        modules.forEach(module -> module.periodic());
    }
}
