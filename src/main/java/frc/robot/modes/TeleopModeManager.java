package frc.robot.modes;

public class TeleopModeManager extends ModeManager {
    private static TeleopModeManager INSTANCE = null;
    public static TeleopModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeleopModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        
    }
}