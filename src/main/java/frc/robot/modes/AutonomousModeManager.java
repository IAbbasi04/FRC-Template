package frc.robot.modes;

public class AutonomousModeManager extends ModeManager {
    private static AutonomousModeManager INSTANCE = null;
    public static AutonomousModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutonomousModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {

    }
}