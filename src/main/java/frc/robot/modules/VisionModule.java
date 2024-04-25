package frc.robot.modules;

import frc.robot.Robot;
import frc.robot.common.Enums.MatchMode;

public class VisionModule extends Module {
    private static VisionModule INSTANCE = null;
    public static VisionModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionModule();
        }
        return INSTANCE;
    }

    private VisionModule() {
        super.addLogger("Vision", Robot.LOG_TO_DASHBOARD);
    }
    
    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}