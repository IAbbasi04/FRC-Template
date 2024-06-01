package frc.robot.subsystems;

import lib.frc8592.MatchMode;
import lib.frc8592.logging.SmartLogger;

public class VisionSubsystem extends Subsystem {
    private static VisionSubsystem INSTANCE = null;
    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private VisionSubsystem() {
        super.logger = new SmartLogger("VisionSubsystem");
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}