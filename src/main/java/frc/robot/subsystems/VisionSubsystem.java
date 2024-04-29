package frc.robot.subsystems;

import frc.robot.Robot;
import lib.frc8592.MatchMode;

public class VisionSubsystem extends Subsystem {
    private static VisionSubsystem INSTANCE = null;
    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private VisionSubsystem() {
        super.addLogger("Vision", Robot.LOG_TO_DASHBOARD);
    }
    
    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}