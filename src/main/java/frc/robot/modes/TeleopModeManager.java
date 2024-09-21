package frc.robot.modes;

import frc.robot.subsystems.TestSubsystem;
import lib.frc8592.controls.xbox.XboxInput;

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
        // super.updateSwerve();
        // super.updateLED();
        if (driverController.isPressing(XboxInput.LEFT_BUMPER)) {
            TestSubsystem.getInstance().setDesiredRPM(2000);
        } else if (driverController.isPressing(XboxInput.RIGHT_BUMPER)) {
            TestSubsystem.getInstance().setDesiredRPM(-2000);
        } else {
            TestSubsystem.getInstance().setDesiredRPM(0);    
        }
    }
}