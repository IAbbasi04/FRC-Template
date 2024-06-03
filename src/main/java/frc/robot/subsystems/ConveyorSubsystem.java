package frc.robot.subsystems;

import lib.frc8592.MatchMode;

public class ConveyorSubsystem extends Subsystem {
    private static ConveyorSubsystem INSTANCE = null;
    public static ConveyorSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new ConveyorSubsystem();
        return INSTANCE;
    }

    private ConveyorSubsystem() {}

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}