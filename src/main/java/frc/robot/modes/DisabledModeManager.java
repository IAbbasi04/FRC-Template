package frc.robot.modes;

public class DisabledModeManager extends ModeManager {
    private static DisabledModeManager INSTANCE = null;
    public static DisabledModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DisabledModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        super.updateLED();
    }
}