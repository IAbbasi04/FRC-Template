package frc.robot.subsystems;

import lib.frc8592.MatchMode;

public class IntakeSubsystem extends Subsystem {
    private static IntakeSubsystem INSTANCE = null;
    public static IntakeSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new IntakeSubsystem();
        return INSTANCE;
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}