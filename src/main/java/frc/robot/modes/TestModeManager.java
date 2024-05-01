package frc.robot.modes;

import frc.robot.autonomous.AutoGenerator;
import frc.robot.subsystems.SwerveSubsystem;

public class TestModeManager extends BaseTeleopModeManager {
    private static TestModeManager INSTANCE = null;
    public static TestModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        super.updateSwerve();
        super.updateLED();
        SwerveSubsystem.getInstance().driveToPose(AutoGenerator.AMP.getPose());
    }
}