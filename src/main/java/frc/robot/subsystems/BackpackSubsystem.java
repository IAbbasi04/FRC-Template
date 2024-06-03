package frc.robot.subsystems;

import lib.frc8592.MatchMode;

public class BackpackSubsystem extends Subsystem {
    private static BackpackSubsystem INSTANCE = null;
    public static BackpackSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new BackpackSubsystem();
        return INSTANCE;
    }

    private BackpackSubsystem() {}

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}
