package frc.robot.subsystems;

import lib.frc8592.MatchMode;

public class ShooterSubsystem extends Subsystem {
    private static ShooterSubsystem INSTANCE = null;
    public static ShooterSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new ShooterSubsystem();
        return INSTANCE;
    }

    private ShooterSubsystem() {}

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}