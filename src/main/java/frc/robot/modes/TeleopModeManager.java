package frc.robot.modes;

public class TeleopModeManager extends BaseTeleopModeManager {
    private static TeleopModeManager INSTANCE = null;
    public static TeleopModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeleopModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        super.updateSwerve();
        super.updateLED();
        super.updateExample();
    }
}