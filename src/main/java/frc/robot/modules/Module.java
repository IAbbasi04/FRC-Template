package frc.robot.modules;

import frc.robot.common.Enums.MatchMode;

public abstract class Module {
    /**
     * Runs at the start of the current match mode
     */
    public abstract void init(MatchMode mode);
    
    /**
     * Runs throughout the entirety of the current match mode
     */
    public abstract void periodic();
}