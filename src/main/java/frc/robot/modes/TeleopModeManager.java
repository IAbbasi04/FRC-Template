package frc.robot.modes;

import frc.robot.controls.InputMap;

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
        this.updateIntake();
    }

    public void updateIntake() {
        if (driverController.isPressing(InputMap.MANIPULATOR.INTAKE)) {
            super.setIntaking();
        } else if (driverController.isPressing(InputMap.MANIPULATOR.OUTAKE)) {
            super.setOutaking();
        } else {
            super.stopAllRollers();
        }
    }
}